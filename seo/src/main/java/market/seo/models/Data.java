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
    @Column(columnDefinition = "BLOB NULL")
    private byte[] contentOne;
    @Column(columnDefinition = "BLOB NULL")
    private byte[] contentTwo;
    @Column(columnDefinition = "BLOB NULL")
    private byte[] contentThree;
}
