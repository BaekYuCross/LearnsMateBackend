package intbyte4.learnsmate.voc.domain.vo.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestSaveVOCVO {
    @JsonProperty("voc_content")
    private String vocContent;

    @JsonProperty("voc_category_code")
    private int vocCategoryCode;

    @JsonProperty("member_code")
    private Long memberCode;
}
