package intbyte4.learnsmate.lecture.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import intbyte4.learnsmate.member.domain.entity.QMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .leftJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .where(builder)
                .orderBy(lecture.createdAt.desc())
                .fetch();

        log.info("Found {} results after filtering", results.size());
        return results;
    }

//    @Override
//    public Page<ResponseFindLectureVO> searchByWithPaging(LectureFilterDTO filterDTO, Pageable pageable) {
//        BooleanBuilder builder = createFilterConditions(filterDTO);
//
//        // 전체 개수 조회
//        Long total = queryFactory
//                .select(lecture.countDistinct())
//                .from(lecture)
//                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
//                .leftJoin(lectureCategory).on(lectureCategoryByLecture.lectureCategory.eq(lectureCategory))
//                .where(builder)
//                .fetchOne();
//
//        // 페이징 처리된 데이터 조회
//        List<ResponseFindLectureVO> content = queryFactory
//                .select(Projections.fields(ResponseFindLectureVO.class,
//                        lecture.lectureCode.as("lectureCode"),
//                        lecture.lectureTitle.as("lectureTitle"),
//                        lectureCategory.lectureCategoryName.stringValue().as("lectureCategoryName"),
//                        lecture.lectureLevel.stringValue().as("lectureLevel"),
//                        lecture.tutor.memberCode.as("tutorCode"),
//                        lecture.tutor.memberName.as("tutorName"),
//                        lecture.lecturePrice.as("lecturePrice"),
//                        lecture.createdAt.as("createdAt"),
//                        lecture.lectureConfirmStatus.as("lectureConfirmStatus"),
//                        lecture.lectureStatus.as("lectureStatus")
//                ))
//                .from(lecture)
//                .leftJoin(lecture.tutor)
//                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
//                .leftJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
//                .where(builder)
//                .offset(pageable.getOffset())   // 페이지 시작점
//                .limit(pageable.getPageSize())  // 페이지 크기
//                .fetch();
//
//        return new PageImpl<>(content, pageable, total != null ? total : 0L);
//    }
    @Override
    public Page<ResponseFindLectureVO> searchByWithPaging(LectureFilterDTO filterDTO, Pageable pageable) {
        QMember tutor = QMember.member;
        QLectureCategory category = QLectureCategory.lectureCategory;
        BooleanBuilder builder = createFilterConditions(filterDTO);

        // 정렬 조건 생성
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(lecture, tutor, category, pageable.getSort());

        // 전체 개수 조회
        Long total = queryFactory
                .select(lecture.countDistinct())
                .from(lecture)
                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .leftJoin(lectureCategory).on(lectureCategoryByLecture.lectureCategory.eq(lectureCategory))
                .where(builder)
                .fetchOne();

        // 페이징 처리된 데이터 조회
        List<ResponseFindLectureVO> content = queryFactory
                .select(Projections.fields(ResponseFindLectureVO.class,
                        lecture.lectureCode.as("lectureCode"),
                        lecture.lectureTitle.as("lectureTitle"),
                        lectureCategory.lectureCategoryName.stringValue().as("lectureCategoryName"),
                        lecture.lectureLevel.stringValue().as("lectureLevel"),
                        lecture.tutor.memberCode.as("tutorCode"),
                        lecture.tutor.memberName.as("tutorName"),
                        lecture.lecturePrice.as("lecturePrice"),
                        lecture.createdAt.as("createdAt"),
                        lecture.lectureConfirmStatus.as("lectureConfirmStatus"),
                        lecture.lectureStatus.as("lectureStatus")
                ))
                .from(lecture)
                .leftJoin(lecture.tutor)
                .leftJoin(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .leftJoin(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .where(builder)
                .orderBy(orderSpecifier)  // 정렬 조건 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<ResponseFindLectureVO> findLecturesWithSorting(Pageable pageable) {
        QLecture lecture = QLecture.lecture;
        QMember tutor = QMember.member;
        QLectureCategoryByLecture categoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;
        QLectureCategory category = QLectureCategory.lectureCategory;

        // 쿼리 생성
        JPAQuery<ResponseFindLectureVO> query = queryFactory
                .select(Projections.constructor(ResponseFindLectureVO.class,
                        lecture.lectureCode,
                        lecture.lectureTitle,
                        tutor.memberCode,
                        tutor.memberName,
                        category.lectureCategoryName.stringValue(),  // Enum -> String 변환
                        lecture.lectureLevel.stringValue(),          // Enum -> String 변환
                        lecture.createdAt,
                        lecture.lecturePrice,
                        lecture.lectureConfirmStatus,
                        lecture.lectureStatus))
                .from(lecture)
                .leftJoin(lecture.tutor, tutor)
                .leftJoin(categoryByLecture).on(categoryByLecture.lecture.lectureCode.eq(lecture.lectureCode))
                .leftJoin(category).on(categoryByLecture.lectureCategory.lectureCategoryCode.eq(category.lectureCategoryCode));

        // 정렬 조건 적용
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(lecture, tutor, category, pageable.getSort());
        query.orderBy(orderSpecifier);

        // 전체 카운트 조회
        long total = query.fetchCount();

        // 페이징 적용
        List<ResponseFindLectureVO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }


    private OrderSpecifier<?> getOrderSpecifier(QLecture lecture, QMember tutor,
                                                QLectureCategory category, Sort sort) {
        if (sort.isEmpty()) {
            return lecture.createdAt.desc();
        }

        Sort.Order order = sort.iterator().next();
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;

        switch (order.getProperty()) {
            case "lecture_code":
                return new OrderSpecifier(direction, lecture.lectureCode);
            case "lecture_title":
                return new OrderSpecifier(direction, lecture.lectureTitle);
            case "tutor_code":
                return new OrderSpecifier(direction, lecture.tutor.memberCode);
            case "tutor_name":
                return new OrderSpecifier(direction, lecture.tutor.memberName);
            case "lecture_category_name":
                return new OrderSpecifier(direction, category.lectureCategoryName);
            case "lecture_level":
                return new OrderSpecifier(direction, lecture.lectureLevel);
            case "created_at":
                return new OrderSpecifier(direction, lecture.createdAt);
            case "lecture_price":
                return new OrderSpecifier(direction, lecture.lecturePrice);
            case "lecture_confirm_status":
                return new OrderSpecifier(direction, lecture.lectureConfirmStatus);
            case "lecture_status":
                return new OrderSpecifier(direction, lecture.lectureStatus);
            default:
                return lecture.createdAt.desc();
        }
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
                .and(betweenLecturePrice(filterDTO.getMinLecturePrice(), filterDTO.getMaxLecturePrice()))
                .and(betweenCreatedAt(filterDTO.getStartCreatedAt(), filterDTO.getEndCreatedAt()));

        return builder;
    }

    private BooleanExpression eqLectureCode(String lectureCode) {
        return StringUtils.hasText(lectureCode) ? lecture.lectureCode.likeIgnoreCase("%" + lectureCode + "%") : null;
    }

    private BooleanExpression likeLectureTitle(String title) {
        return StringUtils.hasText(title) ? lecture.lectureTitle.likeIgnoreCase("%" + title + "%") : null;
    }

    private BooleanExpression eqTutorCode(Long tutorCode) {
        return tutorCode != null ? lecture.tutor.memberCode.eq(tutorCode) : null;
    }

    private BooleanExpression eqTutorName(String tutorName) {
        return StringUtils.hasText(tutorName) ? lecture.tutor.memberName.likeIgnoreCase("%" + tutorName + "%") : null;
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

    private BooleanExpression betweenLecturePrice(Integer minLecturePrice, Integer maxLecturePrice) {
        if (minLecturePrice == null && maxLecturePrice == null) {
            return null;
        }

        BooleanExpression expression = null;
        if (minLecturePrice != null) {
            expression = lecture.lecturePrice.goe(minLecturePrice);
        }
        if (maxLecturePrice != null) {
            expression = expression == null ?
                    lecture.lecturePrice.loe(maxLecturePrice) :
                    expression.and(lecture.lecturePrice.loe(maxLecturePrice));
        }
        return expression;
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
