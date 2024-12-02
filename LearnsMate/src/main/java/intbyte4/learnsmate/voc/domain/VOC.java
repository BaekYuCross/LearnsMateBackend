package intbyte4.learnsmate.voc.domain;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity(name = "Voc")
@Table(name = "voc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VOC {

    @Id
    @Column(name = "voc_code", nullable = false, unique = true)
    private String vocCode;

    @Column(name = "voc_content", nullable = false)
    private String vocContent;

    @Column(name = "voc_answer_status", nullable = false)
    private Boolean vocAnswerStatus;

    @Column(name = "voc_answer_satisfaction")
    private String vocAnswerSatisfaction;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "voc_category_code", nullable = false)
    private VOCCategory vocCategory;

    @ManyToOne
    @JoinColumn(name = "member_code", nullable = false)
    private Member member;

    @PrePersist
    public void generateVocCode() {
        int categoryCode = this.vocCategory.getVocCategoryCode();
        String formattedCategoryCode = String.format("%03d", categoryCode);

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String uniqueCode = UUID.randomUUID().toString().substring(0, 8);

        this.vocCode = String.format("V%s-%s%s", formattedCategoryCode, formattedDate, uniqueCode);
    }

    public void updateSatisfaction(Long satisfaction){
        if(satisfaction == 0){
            this.vocAnswerSatisfaction = "불만족";
        }else if(satisfaction == 1){
            this.vocAnswerSatisfaction = "보통";
        }else{
            this.vocAnswerSatisfaction = "만족";
        }
    }
}
