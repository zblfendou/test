package market.seo.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(indexes = {@Index(name = "IX_KEYWORD", columnList = "keyWord"),
        @Index(name = "IX_INITIAL", columnList = "initial"),
        @Index(name = "IX_DIGIT", columnList = "digit")})
@NoArgsConstructor
public class SeoAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String keyWord;
    @Column(columnDefinition = "char(1)")
    private String initial;
    private String digit;
    @Column(columnDefinition = "BLOB NULL")
    private byte[] data;

    public SeoAnswer(String keyWord, String initial, String digit, byte[] data) {
        this.keyWord = keyWord;
        this.initial = initial;
        this.data = data;
        this.digit = digit;
    }
}
