package market.seo.daos;

import market.seo.models.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.stream.Stream;

public interface DataDao extends JpaRepository<Data, Long> {
    @Query("select d from Data d")
    Stream<Data> getDataStream();
}
