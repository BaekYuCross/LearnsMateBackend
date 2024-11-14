package intbyte4.learnsmate.lecture.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestLectureFilterVO;
import intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static intbyte4.learnsmate.lecture_category.domain.entity.QLectureCategory.lectureCategory;

@Repository
public class CustomLectureRepositoryImpl implements CustomLectureRepository {

    private final JPAQueryFactory queryFactory;

    public CustomLectureRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<LectureFilterDTO> findLecturesByFilters(RequestLectureFilterVO request) {
        QLecture lecture = QLecture.lecture;
        QLectureCategory lectureCategory = QLectureCategory.lectureCategory;
        QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;

        BooleanBuilder builder = new BooleanBuilder()
                .and(eqLectureCode(request))
                .and(likeLectureTitle(request))
                .and(eqTutorCode(request))
                .and(eqTutorName(request))
                .and(eqLectureCategoryName(request))
                .and(eqLectureLevel(request))
                .and(eqLectureConfirmStatus(request))
                .and(eqLectureStatus(request))
                .and(betweenLecturePrice(request))
                .and(betweenCreatedAt(request));

        return queryFactory
                .select(Projections.constructor(
                        LectureFilterDTO.class,
                        lecture.lectureCode,
                        lecture.lectureTitle,
                        lecture.tutor.memberCode,
                        lecture.tutor.memberName,
                        lectureCategory.lectureCategoryName,
                        lecture.lectureLevel,
                        lecture.lectureConfirmStatus,
                        lecture.lectureStatus,
                        lecture.lecturePrice,
                        lecture.createdAt
                ))
                .from(lecture)
                .join(lectureCategoryByLecture).on(lectureCategoryByLecture.lecture.eq(lecture))
                .join(lectureCategoryByLecture.lectureCategory, lectureCategory)
                .where(builder)
                .fetch();
    }

    private BooleanExpression eqLectureCode(RequestLectureFilterVO request) {

        if (request.getLectureCode() == null) {
            return null;
        }
        return QLecture.lecture.lectureCode.eq(request.getLectureCode());
    }

    private BooleanExpression likeLectureTitle(RequestLectureFilterVO request) {

        if (request.getLectureTitle() == null) {
            return null;
        }
        return QLecture.lecture.lectureTitle.likeIgnoreCase("%" + request.getLectureTitle() + "%");
    }

    private BooleanExpression eqTutorCode(RequestLectureFilterVO request) {

        if (request.getTutorCode() == null) {
            return null;
        }
        return QLecture.lecture.tutor.memberCode.eq(request.getTutorCode());
    }

    private BooleanExpression eqTutorName(RequestLectureFilterVO request) {

        if (request.getTutorName() == null) {
            return null;
        }
        return QLecture.lecture.tutor.memberName.eq(request.getTutorName());
    }

    private BooleanExpression eqLectureCategoryName(RequestLectureFilterVO request) {

        if (request.getLectureCategoryName() == null) {
            return null;
        }
        return lectureCategory.lectureCategoryName.eq(LectureCategoryEnum.valueOf(request.getLectureCategoryName()));
    }

    private BooleanExpression eqLectureLevel(RequestLectureFilterVO request) {

        if (request.getLectureLevel() == null) {
            return null;
        }
        return QLecture.lecture.lectureLevel.eq(LectureLevelEnum.valueOf(request.getLectureLevel()));
    }

    private BooleanExpression eqLectureConfirmStatus(RequestLectureFilterVO request) {

        if (request.getLectureConfirmStatus() == null) {
            return null;
        }
        return QLecture.lecture.lectureConfirmStatus.eq(request.getLectureConfirmStatus());
    }

    private BooleanExpression eqLectureStatus(RequestLectureFilterVO request) {

        if (request.getLectureStatus() == null) {
            return null;
        }
        return QLecture.lecture.lectureStatus.eq(request.getLectureStatus());
    }

    private BooleanExpression betweenLecturePrice(RequestLectureFilterVO request) {

        if (request.getMinPrice() == null && request.getMaxPrice() == null) {
            return null;
        }
        if (request.getMinPrice() == null) {
            return QLecture.lecture.lecturePrice.loe(request.getMaxPrice());
        }
        if (request.getMaxPrice() == null) {
            return QLecture.lecture.lecturePrice.gt(request.getMinPrice());
        }
        return QLecture.lecture.lecturePrice.between(request.getMinPrice(), request.getMaxPrice());
    }

    private BooleanExpression betweenCreatedAt(RequestLectureFilterVO request) {

        if (request.getStartCreatedAt() == null && request.getEndCreatedAt() == null) {
            return null;
        }
        if (request.getEndCreatedAt() == null) {
            return QLecture.lecture.createdAt.gt(request.getStartCreatedAt());
        }
        if (request.getStartCreatedAt() == null) {
            return QLecture.lecture.createdAt.lt(request.getEndCreatedAt());
        }
        return QLecture.lecture.createdAt.between(request.getStartCreatedAt(), request.getEndCreatedAt());
    }
}
