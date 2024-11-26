package intbyte4.learnsmate.voc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "VocAiAnswer")
@Table(name = "voc_ai_answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VOCAiAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voc_ai_answer_code")
    private Long vocAiAnswerCode;

    @Column(name = "analysis_date", nullable = false)
    private LocalDate analysisDate;

    @Column(name = "keyword", nullable = false)
    private String keyword;

    @Column(name = "keyword_count", nullable = false)
    private Integer keywordCount;

    @Column(name = "recommendation", nullable = false)
    private String recommendation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
