package market.seo.daos;

import market.seo.models.AppComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppCommentDao extends JpaRepository<AppComment, String> {
    AppComment findByApkName(String apkName);

}
