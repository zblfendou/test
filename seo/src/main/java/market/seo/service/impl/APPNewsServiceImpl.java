package market.seo.service.impl;

import market.seo.daos.APPNewsDao;
import market.seo.models.APPNews;
import market.seo.service.APPNewsService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class APPNewsServiceImpl implements APPNewsService {
    @Inject
    private APPNewsDao dao;

    @Override
    @Transactional
    public void save(List<APPNews> collect) {
        dao.save(collect);
    }
}
