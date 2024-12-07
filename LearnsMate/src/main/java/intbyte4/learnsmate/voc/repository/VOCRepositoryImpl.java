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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class VOCRepositoryImpl implements VOCRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QVOC qVOC = QVOC.vOC;

    @Override
    public List<VOC> findAllByFilter(VOCFilterRequestDTO dto) {
        log.info("Finding all VOCs with filter: {}", dto);

        BooleanExpression[] conditions = createConditions(dto);

        log.info("Applying conditions to findAllByFilter query");
        List<VOC> results = queryFactory
                .selectFrom(qVOC)
                .leftJoin(qVOC.vocCategory).fetchJoin()
                .leftJoin(qVOC.member).fetchJoin()
                .where(conditions)
                .orderBy(qVOC.createdAt.desc())
                .fetch();

        log.info("Found {} results after filtering", results.size());
        return results;
    }

    private BooleanExpression[] createConditions(VOCFilterRequestDTO dto) {
        List<BooleanExpression> conditions = new ArrayList<>();

        if (StringUtils.hasText(dto.getVocCode())) {
            conditions.add(qVOC.vocCode.eq(dto.getVocCode()));
        }

        if (StringUtils.hasText(dto.getVocContent())) {
            conditions.add(searchByContents(dto.getVocContent()));
        }

        if (dto.getVocCategoryCode() != null) {
            conditions.add(qVOC.vocCategory.vocCategoryCode.eq(dto.getVocCategoryCode()));
        }

        if (StringUtils.hasText(dto.getMemberType())) {
            conditions.add(qVOC.member.memberType.eq(MemberType.valueOf(dto.getMemberType())));
        }

        if (dto.getVocAnswerStatus() != null) {
            log.info("Adding answer status condition: {}", dto.getVocAnswerStatus());
            conditions.add(qVOC.vocAnswerStatus.eq(dto.getVocAnswerStatus()));
        }

        if (StringUtils.hasText(dto.getVocAnswerSatisfaction())) {
            conditions.add(qVOC.vocAnswerSatisfaction.eq(dto.getVocAnswerSatisfaction()));
        }

        if (dto.getStartCreateDate() != null && dto.getStartEndDate() != null) {
            conditions.add(qVOC.createdAt.between(dto.getStartCreateDate(), dto.getStartEndDate()));
        } else if (dto.getStartCreateDate() != null) {
            conditions.add(qVOC.createdAt.goe(dto.getStartCreateDate()));
        } else if (dto.getStartEndDate() != null) {
            conditions.add(qVOC.createdAt.loe(dto.getStartEndDate()));
        }

        return conditions.toArray(new BooleanExpression[0]);
    }

    @Override
    public Page<VOC> searchByWithPaging(VOCFilterRequestDTO dto, Pageable pageable) {
        log.info("Searching VOCs with criteria: {} and page: {}", dto, pageable);

        List<VOC> content = queryFactory
                .selectFrom(qVOC)
                .leftJoin(qVOC.vocCategory).fetchJoin()
                .leftJoin(qVOC.member).fetchJoin()
                .where(
                        eqVOCCode(dto.getVocCode()),
                        searchByContents(dto.getVocContent()),
                        searchByType(dto.getVocCategoryCode()),
                        searchByMemberType(dto.getMemberType()),
                        searchByAnswerStatus(dto.getVocAnswerStatus()),
                        searchByAnswerSatisfaction(dto.getVocAnswerSatisfaction()),
                        searchByCreatedAt(dto.getStartCreateDate(), dto.getStartEndDate())
                )
                .orderBy(qVOC.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
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

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression eqVOCCode(String vocCode) {
        if (vocCode == null || vocCode.isBlank()) {
            return null;
        }
        log.info("Filtering by VOC code: {}", vocCode);
        return qVOC.vocCode.eq(vocCode);
    }

    private BooleanExpression searchByContents(String vocContents) {
        if (vocContents == null || vocContents.isBlank()) {
            return null;
        }
        log.info("Filtering by contents: {}", vocContents);
        String[] keywords = vocContents.split(" ");
        BooleanExpression expression = null;

        for (String keyword : keywords) {
            if (!keyword.isBlank()) {
                BooleanExpression condition = qVOC.vocContent.containsIgnoreCase(keyword);
                expression = expression == null ? condition : expression.and(condition);
            }
        }

        return expression;
    }

    private BooleanExpression searchByType(Integer categoryCode) {
        if (categoryCode == null) return null;
        log.info("Filtering by category code: {}", categoryCode);
        return qVOC.vocCategory.vocCategoryCode.eq(categoryCode);
    }

    private BooleanExpression searchByMemberType(String memberType) {
        if (memberType == null || memberType.isBlank()) {
            return null;
        }
        try {
            MemberType type = MemberType.valueOf(memberType);
            log.info("Filtering by member type: {}", type);
            return qVOC.member.memberType.eq(type);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid member type: {}", memberType);
            return null;
        }
    }

    private BooleanExpression searchByAnswerStatus(Boolean answerStatus) {
        if (answerStatus == null) {
            log.info("Answer status is null, skipping filter");
            return null;
        }
        log.info("Filtering by answer status: {}", answerStatus);
        return qVOC.vocAnswerStatus.eq(answerStatus);
    }

    private BooleanExpression searchByAnswerSatisfaction(String satisfaction) {
        if (satisfaction == null || satisfaction.isBlank()) {
            return null;
        }
        log.info("Filtering by satisfaction: {}", satisfaction);
        return qVOC.vocAnswerSatisfaction.eq(satisfaction);
    }

    private BooleanExpression searchByCreatedAt(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }
        log.info("Filtering by date range: {} to {}", startDate, endDate);
        if (startDate != null && endDate != null) {
            return qVOC.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return qVOC.createdAt.goe(startDate);
        } else {
            return qVOC.createdAt.loe(endDate);
        }
    }
}