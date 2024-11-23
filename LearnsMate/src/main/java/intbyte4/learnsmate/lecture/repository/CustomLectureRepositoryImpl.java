package intbyte4.learnsmate.lecture.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomLectureRepositoryImpl implements CustomLectureRepository {
    private final JPAQueryFactory queryFactory;
    private final QLecture lecture = QLecture.lecture;
    private final QLectureCategory lectureCategory = QLectureCategory.lectureCategory;
    private final QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;

    @Override
    public List<Lecture> findAllByFilter(LectureFilterDTO dto) {
        log.info("Finding all Lectures with filter: {}", dto);

        BooleanBuilder builder = createFilterConditions(dto);

        List<Lecture> results = queryFactory
                .selectFrom(lecture)
                .leftJoin(lecture.tutor).fetchJoin()
                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .leftJoin(lectureCategoryByLecture.lectureCategory, lectureCategory).fetchJoin()
                .where(builder)
                .orderBy(lecture.createdAt.desc())
                .fetch();

        log.info("Found {} results after filtering", results.size());
        return results;
    }

    @Override
    public Page<ResponseFindLectureVO> searchByWithPaging(LectureFilterDTO filterDTO, Pageable pageable) {
        BooleanBuilder builder = createFilterConditions(filterDTO);

        Long total = queryFactory
                .select(lecture.countDistinct())
                .from(lecture)
                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .leftJoin(lectureCategory).on(lectureCategoryByLecture.lectureCategory.eq(lectureCategory))
                .where(builder)
                .fetchOne();

        List<ResponseFindLectureVO> content = queryFactory
                .select(Projections.fields(ResponseFindLectureVO.class,
                                lecture.lectureCode.as("lectureCode"),
                                lecture.lectureTitle.as("lectureTitle"),
                                lecture.tutor.memberCode.as("tutorCode"),
                                lecture.tutor.memberName.as("tutorName"),
                                lectureCategory.lectureCategoryName.stringValue().as("lectureCategoryName"),
                                lecture.lectureLevel.stringValue().as("lectureLevel"),
                                lecture.createdAt.as("createdAt"),
                                lecture.lecturePrice.as("lecturePrice"),
                                lecture.lectureConfirmStatus.as("lectureConfirmStatus"),
                                lecture.lectureStatus.as("lectureStatus")
                        )
                )
                .from(lecture)
                .leftJoin(lecture.tutor)
                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .leftJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }


    private BooleanBuilder createFilterConditions(LectureFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(eqLectureCode(filterDTO.getLectureCode()))
                .and(likeLectureTitle(filterDTO.getLectureTitle()))
                .and(eqTutorCode(filterDTO.getTutorCode()))
                .and(eqTutorName(filterDTO.getTutorName()))
                .and(eqLectureCategoryName(filterDTO.getLectureCategoryName()))
                .and(eqLectureLevel(filterDTO.getLectureLevel()))
                .and(eqLectureConfirmStatus(filterDTO.getLectureConfirmStatus()))
                .and(eqLectureStatus(filterDTO.getLectureStatus()))
                .and(betweenLecturePrice(filterDTO.getMinPrice(), filterDTO.getMaxPrice()))
                .and(betweenCreatedAt(filterDTO.getStartCreatedAt(), filterDTO.getEndCreatedAt()));

        return builder;
    }

    private BooleanExpression eqLectureCode(String lectureCode) {
        return StringUtils.hasText(lectureCode) ? lecture.lectureCode.eq(lectureCode) : null;
    }

    private BooleanExpression likeLectureTitle(String title) {
        return StringUtils.hasText(title) ? lecture.lectureTitle.likeIgnoreCase("%" + title + "%") : null;
    }

    private BooleanExpression eqTutorCode(Long tutorCode) {
        return tutorCode != null ? lecture.tutor.memberCode.eq(tutorCode) : null;
    }

    private BooleanExpression eqTutorName(String tutorName) {
        return StringUtils.hasText(tutorName) ? lecture.tutor.memberName.eq(tutorName) : null;
    }

    private BooleanExpression eqLectureCategoryName(String categoryName) {
        return StringUtils.hasText(categoryName) ? lectureCategory.lectureCategoryName.stringValue().eq(categoryName.toUpperCase()) : null;
    }

    private BooleanExpression eqLectureLevel(String level) {
        return StringUtils.hasText(level) ? lecture.lectureLevel.stringValue().eq(level.toUpperCase()) : null;
    }

    private BooleanExpression eqLectureConfirmStatus(Boolean status) {
        return status != null ? lecture.lectureConfirmStatus.eq(status) : null;
    }

    private BooleanExpression eqLectureStatus(Boolean status) {
        return status != null ? lecture.lectureStatus.eq(status) : null;
    }

    private BooleanExpression betweenLecturePrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null && maxPrice == null) {
            return null;
        }
        if (minPrice == null) {
            return lecture.lecturePrice.loe(maxPrice);
        }
        if (maxPrice == null) {
            return lecture.lecturePrice.goe(minPrice);
        }
        return lecture.lecturePrice.between(minPrice, maxPrice);
    }

    private BooleanExpression betweenCreatedAt(LocalDate startCreatedAt, LocalDate endCreatedAt) {
        LocalDateTime startDateTime = (startCreatedAt != null) ? startCreatedAt.atStartOfDay() : null;
        LocalDateTime endDateTime = (endCreatedAt != null) ? endCreatedAt.atTime(23, 59, 59) : null;

        if (startDateTime == null && endDateTime == null) {
            return null;
        }
        if (startDateTime == null) {
            return lecture.createdAt.loe(endDateTime);
        }
        if (endDateTime == null) {
            return lecture.createdAt.goe(startDateTime);
        }
        return lecture.createdAt.between(startDateTime, endDateTime);
    }
}
