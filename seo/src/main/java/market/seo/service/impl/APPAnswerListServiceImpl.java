package market.seo.service.impl;

import market.seo.daos.APPAnswerListDao;
import market.seo.models.APPAnswerList;
import market.seo.service.APPAnswerListService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class APPAnswerListServiceImpl implements APPAnswerListService {
    @Inject
    private APPAnswerListDao dao;

    @Override
    @Transactional
    public void save(List<APPAnswerList> list) {
        dao.save(list);
    }
}
