package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class APP implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    private String link;
    private String title;
    private String score;
    private String classify;//分类
    private String downloadCount;//下载量
    private String qrCodeAddress;//二维码地址
    private String coverAddress;//封面地址
    private String desc;//简介
    private String logoAddress;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String comment;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String news;
}
