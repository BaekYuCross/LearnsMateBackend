package intbyte4.learnsmate.voc.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.voc.domain.QVOC;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class VOCRepositoryImpl implements VOCRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QVOC qVOC = QVOC.vOC;

    @Override
    public List<VOC> searchBy(VOCFilterRequestDTO dto) {
        return queryFactory
                .selectFrom(qVOC)
                .where(
                        eqVOCCode(dto.getVocCode()),
                        searchByContents(dto.getVocContent()),
                        searchByType(dto.getVocCategoryCode()),
                        searchByMemberType(dto.getMemberType()),
                        searchByAnswerStatus(dto.getVocAnswerStatus()),
                        searchByAnswerSatisfaction(dto.getVocAnswerSatisfaction()),
                        searchByCreatedAt(dto.getStartCreateDate(), dto.getStartEndDate())
                )
                .fetch();
    }

    @Override
    public Page<VOC> searchByWithPaging(VOCFilterRequestDTO dto, Pageable pageable) {
        List<VOC> content = queryFactory
                .selectFrom(qVOC)
                .where(
                        eqVOCCode(dto.getVocCode()),
                        searchByContents(dto.getVocContent()),
                        searchByType(dto.getVocCategoryCode()),
                        searchByMemberType(dto.getMemberType()),
                        searchByAnswerStatus(dto.getVocAnswerStatus()),
                        searchByAnswerSatisfaction(dto.getVocAnswerSatisfaction()),
                        searchByCreatedAt(dto.getStartCreateDate(), dto.getStartEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(qVOC.count())
                .from(qVOC)
                .where(
                        eqVOCCode(dto.getVocCode()),
                        searchByContents(dto.getVocContent()),
                        searchByType(dto.getVocCategoryCode()),
                        searchByMemberType(dto.getMemberType()),
                        searchByAnswerStatus(dto.getVocAnswerStatus()),
                        searchByAnswerSatisfaction(dto.getVocAnswerSatisfaction()),
                        searchByCreatedAt(dto.getStartCreateDate(), dto.getStartEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        if (startDate != null && endDate != null) {
            return qVOC.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return qVOC.createdAt.goe(startDate);
        } else {
            return qVOC.createdAt.loe(endDate);
        }
    }

    private BooleanExpression searchByAnswerSatisfaction(String vocAnswerSatisfaction) {
        if (vocAnswerSatisfaction == null || vocAnswerSatisfaction.isBlank()) {
            return null;
        }
        return qVOC.vocAnswerSatisfaction.eq(vocAnswerSatisfaction);
    }

    private BooleanExpression eqVOCCode(String vocCode) {
        if (vocCode == null || vocCode.isBlank()) {
            return null;
        }
        try {
            return qVOC.vocCode.eq(vocCode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private BooleanExpression searchByContents(String vocContents) {
        if (vocContents == null || vocContents.isBlank()) {
            return null;
        }

        String[] keywords = vocContents.split(" ");
        BooleanExpression expression = null;

        for (String keyword : keywords) {
            if (!keyword.isBlank()) {
                BooleanExpression condition = qVOC.vocContent.containsIgnoreCase(keyword);
                expression = expression == null ? condition : expression.or(condition);
            }
        }

        return expression;
    }

    private BooleanExpression searchByType(Integer categoryCode) {
        return categoryCode == null ? null : qVOC.vocCategory.vocCategoryCode.eq(categoryCode);
    }

    private BooleanExpression searchByMemberType(String memberType) {
        if (memberType == null || memberType.isBlank()) {
            return null;
        }
        try {
            return qVOC.member.memberType.eq(MemberType.valueOf(memberType));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private BooleanExpression searchByAnswerStatus(Boolean answerStatus) {
        if (answerStatus == null) {
            return null;
        }
        log.info("Filtering answerStatus: {}", answerStatus);
        return qVOC.vocAnswerStatus.eq(answerStatus);
    }
}
