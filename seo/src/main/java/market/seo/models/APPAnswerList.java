package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class APPAnswerList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    private String keyword;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> list;
}
