package market.seo.service;

import market.seo.daos.AppCommentDao;
import market.seo.models.AppComment;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class AppCommentServiceImpl implements AppCommentService {
    @Inject
    private AppCommentDao dao;

    @Override
    @Transactional
    public void save(List<AppComment> list) {
        dao.save(list);
    }
}
