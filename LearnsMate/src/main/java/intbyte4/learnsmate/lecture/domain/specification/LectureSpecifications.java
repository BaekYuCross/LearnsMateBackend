package intbyte4.learnsmate.lecture.domain.specification;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.domain.Specification;

public class LectureSpecifications {
    public static Specification<Lecture> filterByCriteria(LectureFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (filter.getLectureCode() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("lectureCode"), filter.getLectureCode()));
            }
            if (filter.getLectureTitle() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(root.get("lectureTitle"), "%" + filter.getLectureTitle() + "%"));
            }
            if (filter.getTutorCode() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("tutorCode"), filter.getTutorCode()));
            }
            // 강사 명 필터 조인
            if (filter.getCreatedAt() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
            }
            // 강의 계약 테이블의 강의 계약 단계 필터 조인
            if (filter.getLectureLevel() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("lectureLevel"), filter.getLectureLevel()));
            }
            if (filter.getLecturePrice() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("lecturePrice"), filter.getLecturePrice()));
            }
            if (filter.getLectureStatus() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("lectureStatus"), filter.getLectureStatus()));
            }

            return predicates;
        };
    }
}
