package market.seo.daos;

import market.seo.models.AnswerList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerListDao extends JpaRepository<AnswerList, Long> {
}
