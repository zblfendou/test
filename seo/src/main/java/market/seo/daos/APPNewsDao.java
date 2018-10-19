package market.seo.daos;

import market.seo.models.APPNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APPNewsDao extends JpaRepository<APPNews, Long> {
}
