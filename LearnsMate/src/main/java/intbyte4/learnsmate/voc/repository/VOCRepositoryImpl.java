package intbyte4.learnsmate.voc.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.voc.domain.QVOC;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc_answer.domain.QVOCAnswer;
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
    private final QVOCAnswer qVOCAnswer = QVOCAnswer.vOCAnswer;

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

    // 동적 정렬 관련
    private OrderSpecifier<?>[] getSortedColumn(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

            pageable.getSort().stream().forEach(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "vocCode":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.vocCode));
                        break;
                    case "vocContent":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.vocContent));
                        break;
                    case "vocCategoryName":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.vocCategory.vocCategoryName));
                        break;
                    case "memberType":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.member.memberType));
                        break;
                    case "memberName":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.member.memberName));
                        break;
                    case "memberCode":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.member.memberCode));
                        break;
                    case "adminName":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOCAnswer.admin.adminName));
                        break;
                    case "createdAt":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.createdAt));
                        break;
                    case "vocAnswerStatus":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.vocAnswerStatus));
                        break;
                    case "vocAnswerSatisfaction":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, qVOC.vocAnswerSatisfaction));
                        break;
                    default:
                        log.warn("Unsupported sort property: {}, falling back to default sort", order.getProperty());
                        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, qVOC.createdAt));
                        break;
                }
            });

            return orderSpecifiers.toArray(new OrderSpecifier[0]);
        }

        return new OrderSpecifier[]{ new OrderSpecifier<>(Order.DESC, qVOC.createdAt) };
    }

    @Override
    public Page<VOC> findAllBeforeNowWithSort(LocalDateTime now, Pageable pageable) {
        log.info("Finding all VOCs before: {} with sorting", now);

        // adminName 정렬인 경우 추가 join 필요
        boolean needsAdminJoin = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("adminName"));

        JPAQuery<VOC> query = queryFactory
                .selectFrom(qVOC)
                .leftJoin(qVOC.vocCategory).fetchJoin()
                .leftJoin(qVOC.member).fetchJoin();

        // adminName 정렬을 위한 join
        if (needsAdminJoin) {
            query.leftJoin(qVOCAnswer).on(qVOCAnswer.voc.eq(qVOC))
                    .leftJoin(qVOCAnswer.admin);
        }

        List<VOC> content = query
                .where(qVOC.createdAt.loe(now))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortedColumn(pageable))
                .fetch();

        // 전체 카운트 조회
        Long total = queryFactory
                .select(qVOC.count())
                .from(qVOC)
                .where(qVOC.createdAt.loe(now))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Page<VOC> searchByWithPagingWithSort(VOCFilterRequestDTO dto, Pageable pageable) {
        log.info("Searching VOCs with criteria: {} and page: {} with sorting", dto, pageable);

        // 기본 쿼리 생성
        JPAQuery<VOC> query = queryFactory
                .selectFrom(qVOC)
                .leftJoin(qVOC.vocCategory).fetchJoin()
                .leftJoin(qVOC.member).fetchJoin();

        // adminName 정렬이 필요한 경우 추가 조인
        if (pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("adminName"))) {
            query.leftJoin(qVOCAnswer).on(qVOCAnswer.voc.eq(qVOC))
                    .leftJoin(qVOCAnswer.admin);
        }

        // 조건절 적용
        List<VOC> content = query
                .where(
                        eqVOCCode(dto.getVocCode()),
                        searchByContents(dto.getVocContent()),
                        searchByType(dto.getVocCategoryCode()),
                        searchByMemberType(dto.getMemberType()),
                        searchByAnswerStatus(dto.getVocAnswerStatus()),
                        searchByAnswerSatisfaction(dto.getVocAnswerSatisfaction()),
                        searchByCreatedAt(dto.getStartCreateDate(), dto.getStartEndDate())
                )
                .orderBy(getSortedColumn(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 조회
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
}