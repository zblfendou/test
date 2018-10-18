package seo.market.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CommentDetail implements Serializable {
    private String nickName;
    private String content;
}
