package intbyte4.learnsmate.preferred_topics.domain.dto;


import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PreferredTopicsDTO {
    private Long preferredTopicCode;
    private Long memberCode;
    private Integer lectureCategoryCode;

}
