package intbyte4.learnsmate.preferred_topics.domain.dto;


import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Service
@Builder
@ToString
public class PreferredTopicsDTO {
    private Long preferredTopicCode;
    private Long memberCode;
    private Integer lectureCategoryCode;

}
