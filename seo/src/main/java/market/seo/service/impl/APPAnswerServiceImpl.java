package market.seo.service.impl;

import market.seo.daos.APPAnswerDao;
import market.seo.models.APPAnswer;
import market.seo.service.APPAnswerService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class APPAnswerServiceImpl implements APPAnswerService {
    @Inject
    private APPAnswerDao dao;

    @Override
    @Transactional
    public void save(List<APPAnswer> list) {
        dao.save(list);
    }
}
