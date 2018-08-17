package market.seo.daos;

import market.seo.models.SeoAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeoAnswerDao extends JpaRepository<SeoAnswer,Long> {
}
