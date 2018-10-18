package seo.market.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Obj implements Serializable {
    private Integer total;
    private String contextData;
    private Integer hasNext;
    private Integer ret;
    private List<CommentDetail> commentDetails;

}
