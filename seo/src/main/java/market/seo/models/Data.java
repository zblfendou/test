package market.seo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long dataID;
    private long taskID;
    private String titleOne;
    private String titleTwo;
    private String keyword;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String contentOne;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String contentTwo;
    @Column(columnDefinition = "LONGTEXT NULL")
    private String contentThree;

    private String contentHashCode;
    private String titleHashCode;
}
