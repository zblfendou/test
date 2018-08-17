package market.seo.service;

import market.seo.models.Data;
import market.seo.models.SeoAnswer;

import java.util.List;

public interface DataService {
    void save(List<Data> datas);

    void deleteAll();


    void washDataAndSaveSeoAnswers();

    List<SeoAnswer> getAllSeoAnswer();
}
