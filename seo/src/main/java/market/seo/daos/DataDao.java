package market.seo.daos;

import market.seo.models.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.stream.Stream;

public interface DataDao extends JpaRepository<Data, Long> {
    @Query("select d from Data d where d.keyword=?1 order by d.dataID desc,d.taskID asc")
    Stream<Data> getDataStream(String keyword);
    @Query("select d from Data d")
    Stream<Data> getDataStream();

    @Query("select distinct d.keyword from Data d ")
    Set<String> getAllDistinctKeyWords();
}
