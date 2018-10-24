package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
public class APPAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    @Column(columnDefinition = "varchar(2000) NULL")
    private String keyword;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String title;
    private int urlHashCode;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String content;
}
