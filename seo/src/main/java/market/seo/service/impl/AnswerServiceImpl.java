package market.seo.service.impl;

import market.seo.daos.AnswerDao;
import market.seo.daos.AnswerListDao;
import market.seo.daos.DataDao;
import market.seo.models.Answer;
import market.seo.models.AnswerList;
import market.seo.models.Data;
import market.seo.service.AnswerService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

@Named
public class AnswerServiceImpl implements AnswerService {
    @Inject
    private AnswerListDao listDao;
    @Inject
    private AnswerDao answerDao;
    @Inject
    private DataDao dataDao;

    @Override
    @Transactional
    public void save(List<AnswerList> collect) {
        listDao.save(collect);
    }

    @Override
    @Transactional
    public void saveAnswers(List<Answer> collect) {
        answerDao.save(collect);
    }

    @Override
    @Transactional
    public void mergeData() {
        List<Answer> answers = answerDao.findAll();
        List<AnswerList> answerListList = listDao.findAll();
        for (Answer answer : answers) {
            int answerHashCode = answer.getHashCode();
            for (AnswerList ali : answerListList) {
                if (ali.getListHashCode().contains(answerHashCode)) {
                    String keyword = ali.getKeyword();
                    Data data = new Data();
                    data.setKeyword(keyword);
                    data.setContentOne(answer.getContent());
                    data.setContentHashCode(String.valueOf(answer.getContent().hashCode()));
                    data.setDataID(ali.getDataID());
                    data.setTitleOne(answer.getTitle());
                    data.setTitleHashCode(String.valueOf(answer.getTitle().hashCode()));
                    data.setTaskID(1);
                    dataDao.save(data);
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        answerDao.deleteAll();
        listDao.deleteAll();
    }
}
