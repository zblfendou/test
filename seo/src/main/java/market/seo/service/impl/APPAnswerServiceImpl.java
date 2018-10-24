package market.seo.service.impl;

import market.seo.daos.APPAnswerDao;
import market.seo.models.APP;
import market.seo.models.APPAnswer;
import market.seo.models.APPAnswerList;
import market.seo.service.APPAnswerListService;
import market.seo.service.APPAnswerService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class APPAnswerServiceImpl implements APPAnswerService {
    @Inject
    private APPAnswerDao dao;
    @Inject
    private APPAnswerListService listService;

    @Override
    @Transactional
    public void save(List<APPAnswer> list) {
        dao.save(list);
    }

    @Override
    @Transactional
    public void fillKeywordFromAppAnswerList() {
        List<APPAnswer> answers = dao.findAll();
        List<APPAnswerList> lists = listService.getAll();
        for (APPAnswer a : answers) {
            int urlHashCode = a.getUrlHashCode();
            for (APPAnswerList li : lists) {
                if (li.getList().contains(urlHashCode)) {
                    String keyword = li.getKeyword();
                    if (StringUtils.hasText(keyword)) {
                        a.setKeyword(keyword);
                        return;
                    }
                }
            }
            dao.save(a);
        }
    }
}
