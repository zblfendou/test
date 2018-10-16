package market.seo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(indexes = {@Index(name = "IX_KEYWORD", columnList = "keyWord"),
        @Index(name = "IX_INITIAL", columnList = "initial")})
public class SeoAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Version
    @JsonIgnore
    private long version;
    private String keyWord;
    @Column(columnDefinition = "char(1)")
    private String initial;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String data;

    public SeoAnswer() {
    }

    public SeoAnswer(String keyWord, String initial, String data) {
        this.keyWord = keyWord;
        this.initial = initial;
        this.data = data;
    }
}
