package market.seo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.seo.daos.DataDao;
import market.seo.daos.SeoAnswerDao;
import market.seo.models.Data;
import market.seo.models.SeoAnswer;
import market.seo.service.DataService;
import market.seo.utils.PinyinUtils;
import market.seo.vo.SeoDataVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;

@Named
public class DataServiceImpl implements DataService {
    @Inject
    private DataDao dataDao;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private SeoAnswerDao seoAnswerDao;

    @Override
    @Transactional
    public void save(List<Data> datas) {
        dataDao.save(datas);
    }

    @Override
    @Transactional
    public void deleteAll() {
        dataDao.deleteAll();
    }

    @Override
    @Transactional
    public void washDataAndSaveSeoAnswers() {
        Stream<Data> stream = dataDao.getDataStream();
        Map<String, List<Data>> listMap = stream.filter(d -> {
            boolean hasTitle = hasText(d.getTitleOne()) || hasText(d.getTitleTwo());
            boolean hasContent = d.getContentOne() != null
                    || d.getContentTwo() != null
                    || d.getContentThree() != null;
            return hasTitle && hasContent;
        }).collect(Collectors.groupingBy(Data::getKeyword));
        Map<String, List<Data>> lastMap = new HashMap<>();
        listMap.forEach((k, v) -> {
            int vSize = v.size();
            if (vSize < 30) return;
            List<Data> lastList = new ArrayList<>();
            List<String> titleHashCodes = new ArrayList<>();
            List<String> contentHashCodes = new ArrayList<>();
            for (int j = 0; j < vSize; j += 3) {
                if (lastList.size() == 10) {
                    System.out.println("listSize:" + lastList.size());
                    lastMap.put(k, lastList);
                    break;
                }
                Data data = v.get(j);
                String titleHashCode = data.getTitleHashCode();
                String contentHashCode = data.getContentHashCode();
                if (!titleHashCodes.contains(titleHashCode) && !contentHashCodes.contains(contentHashCode)) {
                    titleHashCodes.add(titleHashCode);
                    contentHashCodes.add(contentHashCode);
                    lastList.add(data);
                }
            }

        });
        ArrayList<SeoAnswer> seoAnswers = new ArrayList<>();
        lastMap.forEach((keyWord, list) -> {
            if (list.size() != 10) return;
            List<SeoDataVO> seoDataVOS = new ArrayList<>();
            list.forEach(data -> {
                String titleOne = data.getTitleOne();
                String title = hasText(titleOne) ? titleOne : data.getTitleTwo();
                String contentOne = data.getContentOne();
                String contentTwo = data.getContentTwo();
                String content = hasText(contentOne) ? contentOne : hasText(contentTwo) ? contentTwo : data.getContentThree();
                seoDataVOS.add(new SeoDataVO(title, content.replaceAll("<li", "<p").replaceAll("</li>", "</p>").replaceAll("<ul", "<p").replaceAll("</ul>", "</p>")));
            });
            try {
                String initial = PinyinUtils.getPinYinFirstChart(keyWord);
                String digit = getNumbers(keyWord);
                String data = objectMapper.writeValueAsString(seoDataVOS);
                seoAnswers.add(new SeoAnswer(keyWord, initial, data));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        seoAnswerDao.save(seoAnswers);
    }

    //截取数字
    private String getNumbers(String content) {

        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(content);
        return matcher.replaceAll("");
    }

    @Override
    public List<SeoAnswer> getAllSeoAnswer() {
        return seoAnswerDao.findAll();
    }
}
