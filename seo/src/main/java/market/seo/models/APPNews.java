package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class APPNews implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String title;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String content;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String condition_;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String appName;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String pageUrl;
}
