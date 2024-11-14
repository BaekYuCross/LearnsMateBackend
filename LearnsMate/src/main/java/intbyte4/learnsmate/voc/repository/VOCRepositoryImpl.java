package intbyte4.learnsmate.voc.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.QVOC;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VOCRepositoryImpl implements VOCRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QVOC qVOC = QVOC.vOC;

    @Override
    public List<VOC> searchBy(VOCDTO request, MemberDTO memberDTO) {
        return queryFactory
                .selectFrom(qVOC)
                .where(
                        eqMemberCode(request.getMemberCode()),
                        searchByContents(request.getVocContent()),
                        searchByType(request.getVocCategoryCode()),
                        searchByMemberType(memberDTO),
                        searchByAnswerStatus(request.getVocAnswerStatus())
                )
                .fetch();
    }

    private BooleanExpression eqMemberCode(Long memberCode) {
        return memberCode == null ? null : qVOC.member.memberCode.eq(memberCode);
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

    private BooleanExpression searchByMemberType(MemberDTO memberDTO) {
        return (memberDTO != null && memberDTO.getMemberType() != null)
                ? qVOC.member.memberType.eq(memberDTO.getMemberType())
                : null;
    }

    private BooleanExpression searchByAnswerStatus(Boolean answerStatus) {
        return answerStatus == null ? null : qVOC.vocAnswerStatus.eq(answerStatus);
    }
}
