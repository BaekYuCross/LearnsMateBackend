package intbyte4.learnsmate.lecture.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.entity.QLecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseLectureFilterRequestVO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.QLectureCategoryByLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomLectureRepositoryImpl implements CustomLectureRepository {

    private final JPAQueryFactory queryFactory;
    private final QLecture lecture = QLecture.lecture;
    private final QLectureCategoryByLecture lectureCategoryByLecture = QLectureCategoryByLecture.lectureCategoryByLecture;

    @Override
    public List<Lecture> findLecturesByFilters(ResponseLectureFilterRequestVO request) {
        return queryFactory
                .selectFrom(lecture)
                .where(
                        filterByLectureCode(request),
                        filterByLectureTitle(request),
                        filterByTutorCode(request),
                        filterByTutorName(request),
                        filterByLectureCategory(request),
                        filterByLectureLevel(request),
                        filterByLectureConfirmStatus(request),
                        filterByLectureStatus(request),
                        filterByPriceRange(request),
                        filterByCreationDate(request)
                )
                .fetch();
    }

    private BooleanBuilder filterByLectureCode(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureCode() != null) {
            builder.and(lecture.lectureCode.eq(request.getLectureCode()));
        }
        return builder;
    }

    private BooleanBuilder filterByLectureTitle(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureTitle() != null) {
            builder.and(lecture.lectureTitle.likeIgnoreCase("%" + request.getLectureTitle() + "%"));
        }
        return builder;
    }

    private BooleanBuilder filterByTutorCode(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getTutorCode() != null) {
            builder.and(lecture.tutor.memberCode.eq(request.getTutorCode()));
        }
        return builder;
    }

    private BooleanBuilder filterByTutorName(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getTutorName() != null) {
            builder.and(lecture.tutor.memberName.likeIgnoreCase("%" + request.getTutorName() + "%"));
        }
        return builder;
    }

    private BooleanBuilder filterByLectureCategory(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureCategory() != null) {
            builder.and(lectureCategoryByLecture.lectureCategory.lectureCategoryName.eq(LectureCategoryEnum.valueOf(request.getLectureCategory())));
        }
        return builder;
    }

    private BooleanBuilder filterByLectureLevel(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureLevel() != null) {
            builder.and(lecture.lectureLevel.eq(request.getLectureLevel()));
        }
        return builder;
    }

    private BooleanBuilder filterByLectureConfirmStatus(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureConfirmStatus() != null) {
            builder.and(lecture.lectureConfirmStatus.eq(request.getLectureConfirmStatus()));
        }
        return builder;
    }

    private BooleanBuilder filterByLectureStatus(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getLectureStatus() != null) {
            builder.and(lecture.lectureStatus.eq(request.getLectureStatus()));
        }
        return builder;
    }

    private BooleanBuilder filterByPriceRange(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            builder.and(lecture.lecturePrice.between(request.getMinPrice(), request.getMaxPrice()));
        }
        return builder;
    }

    private BooleanBuilder filterByCreationDate(ResponseLectureFilterRequestVO request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.getStartCreatedAt() != null) {
            builder.and(lecture.createdAt.goe(request.getStartCreatedAt()));
        }
        if (request.getEndCreatedAt() != null) {
            builder.and(lecture.createdAt.loe(request.getEndCreatedAt()));
        }
        return builder;
    }
}
