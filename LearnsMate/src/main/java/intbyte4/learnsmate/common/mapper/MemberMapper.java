package intbyte4.learnsmate.common.mapper;

import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;

public class MemberMapper implements GenericMapper<MemberDTO, Member>{

    @Override
    public MemberDTO toDTO(Member entity) {
        return MemberDTO.builder()
                .memberCode(entity.getMemberCode())
                .memberType(entity.getMemberType())
                .memberEmail(entity.getMemberEmail())
                .memberPassword(entity.getMemberPassword())
                .memberName(entity.getMemberName())
                .memberAge(entity.getMemberAge())
                .memberPhone(entity.getMemberPhone())
                .memberAddress(entity.getMemberAddress())
                .memberBirth(entity.getMemberBirth())
                .memberFlag(entity.getMemberFlag())
                .memberDormantStatus(entity.getMemberDormantStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public Member toEntity(MemberDTO dto) {
        return Member.builder()
                .memberCode(dto.getMemberCode())
                .memberType(dto.getMemberType())
                .memberEmail(dto.getMemberEmail())
                .memberPassword(dto.getMemberPassword())
                .memberName(dto.getMemberName())
                .memberAge(dto.getMemberAge())
                .memberPhone(dto.getMemberPhone())
                .memberAddress(dto.getMemberAddress())
                .memberBirth(dto.getMemberBirth())
                .memberFlag(dto.getMemberFlag())
                .memberDormantStatus(dto.getMemberDormantStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
