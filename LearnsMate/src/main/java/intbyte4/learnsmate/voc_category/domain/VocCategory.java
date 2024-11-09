package intbyte4.learnsmate.voc_category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "VocCategory")
@Table(name = "voc_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VocCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voc_category_code", nullable = false, unique = true)
    private Integer vocCategoryCode;

    @Column(name = "voc_category_name", nullable = false)
    private String vocCategoryName;
}
