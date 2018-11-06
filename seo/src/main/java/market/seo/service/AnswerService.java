package market.seo.service;

import market.seo.models.Answer;
import market.seo.models.AnswerList;

import java.util.List;

public interface AnswerService {
    void save(List<AnswerList> collect);

    void saveAnswers(List<Answer> collect);

    void mergeData();

    void deleteAll();
}
