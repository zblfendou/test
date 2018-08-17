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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;

@Named
public class DataServiceImpl implements DataService {
    public static final Charset UTF_8 = StandardCharsets.UTF_8;
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
            if (v.size() < 30) return;
            List<Data> lastList = new ArrayList<>();
            for (int j = 0; j < 30; j += 3) {
                lastList.add(v.get(j));
            }
            lastMap.put(k, lastList);
        });
        ArrayList<SeoAnswer> seoAnswers = new ArrayList<>();
        lastMap.forEach((keyWord, list) -> {
            ArrayList<SeoDataVO> seoDataVOS = new ArrayList<>();
            list.forEach(data -> {
                String titleOne = data.getTitleOne();
                String title = hasText(titleOne) ? titleOne : data.getTitleTwo();
                byte[] contentOne = data.getContentOne();
                byte[] contentTwo = data.getContentTwo();
                String content = contentOne != null && contentOne.length > 0 ? new String(contentOne, UTF_8) : contentTwo != null && contentTwo.length > 0 ? new String(contentTwo, UTF_8) : new String(data.getContentThree(), UTF_8);
                seoDataVOS.add(new SeoDataVO(title, content));
            });
            try {
                String initial = PinyinUtils.getPinYinFirstChart(keyWord);
                String digit = getNumbers(keyWord);
                if (StringUtils.hasText(digit)){
                    System.out.println(initial+"  "+digit);
                }
                seoAnswers.add(new SeoAnswer(keyWord, initial, digit, objectMapper.writeValueAsString(seoDataVOS).getBytes(UTF_8)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        seoAnswerDao.save(seoAnswers);

        /*Set<String> keywords = dataDao.getAllDistinctKeyWords();
        keywords.forEach(k->{
            Stream<Data> dataStream = dataDao.getDataStream(k);
            *//*AtomicInteger atomicInteger = new AtomicInteger();
            dataStream.forEach(d->{
                if (atomicInteger.getAndIncrement()%3==0&&){

                }
            });*//*
        });*/
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
