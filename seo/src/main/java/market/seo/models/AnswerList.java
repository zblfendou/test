package market.seo.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class AnswerList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    private long version;
    private long dataID;
    @Column(columnDefinition = "varchar(500) null")
    private String keyword;
    @ElementCollection
    private List<Integer> listHashCode;
}
