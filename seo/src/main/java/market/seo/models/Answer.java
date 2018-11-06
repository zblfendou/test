package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Answer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    private int hashCode;
    @Column(columnDefinition = "varchar(2000) null")
    private String title;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String content;
}
