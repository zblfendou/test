package market.seo.service;

import market.seo.models.APPAnswerList;

import java.util.List;

public interface APPAnswerListService {
    void save(List<APPAnswerList> list);

    List<APPAnswerList> getAll();

}
