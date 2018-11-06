package market.seo.service.impl;

import market.seo.daos.APPAnswerListDao;
import market.seo.models.APPAnswerList;
import market.seo.service.APPAnswerListService;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class APPAnswerListServiceImpl implements APPAnswerListService {
    @Inject
    private APPAnswerListDao dao;

    @Override
    @Transactional
    public void save(List<APPAnswerList> list) {
        dao.save(list);
    }

    @Override
    public List<APPAnswerList> getAll() {
        return dao.findAll();
    }

    @Override
    public void changeUrlToHashCode() {
        List<APPAnswerList> all = dao.findAll();
        all.forEach(li -> {
            li.setList(li.getList().stream().map(li_ -> String.valueOf(li_.hashCode())).collect(Collectors.toList()));
            dao.save(li);
        });
    }
}
