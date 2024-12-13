package intbyte4.learnsmate.admin.domain.vo.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseEmailMessageVO {

    @JsonProperty("message")
    private String message;
}
