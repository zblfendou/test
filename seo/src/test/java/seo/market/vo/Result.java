package seo.market.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Result implements Serializable {

    private Integer total;
    private Integer count;
    private Obj obj;

}
