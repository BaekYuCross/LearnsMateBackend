package intbyte4.learnsmate.member.mapper;

import intbyte4.learnsmate.campaign.domain.vo.request.RequestEditCampaignStudentVO;
import intbyte4.learnsmate.campaign.domain.vo.request.RequestFindCampaignStudentVO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.vo.request.RequestEditMemberVO;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MemberMapper{

    public MemberDTO fromMembertoMemberDTO(Member entity) {
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

    // VO -> DTO 변환 메서드
    public MemberDTO fromRequestSaveMemberVOtoMemberDTO(RequestSaveMemberVO request) {
        return MemberDTO.builder()
                .memberType(request.getMemberType())
                .memberEmail(request.getMemberEmail())
                .memberPassword(request.getMemberPassword())
                .memberName(request.getMemberName())
                .memberAge(request.getMemberAge())
                .memberPhone(request.getMemberPhone())
                .memberAddress(request.getMemberAddress())
                .memberBirth(request.getMemberBirth())
                .memberFlag(true) // 기본 값 설정
                .memberDormantStatus(false) // 기본 값 설정
                .createdAt(LocalDateTime.now()) // 생성 시점
                .updatedAt(LocalDateTime.now()) // 생성 시점
                .build();
    }

    public Member fromMemberDTOtoMember(MemberDTO dto) {
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

    // MemberDTO -> ResponseFindMemberVO 변환 메서드
    public ResponseFindMemberVO fromMemberDTOtoResponseFindMemberVO(MemberDTO dto) {
        return ResponseFindMemberVO.builder()
                .memberCode(dto.getMemberCode())
                .memberType(dto.getMemberType())
                .memberEmail(dto.getMemberEmail())
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

    // RequestEditMemberVO -> MemberDTO 변환
    public MemberDTO fromRequestEditMemberVOToMemberDTO(RequestEditMemberVO request) {
        return MemberDTO.builder()
                .memberCode(request.getMemberCode())
                .memberType(request.getMemberType())
                .memberEmail(request.getMemberEmail())
//                .memberPassword(request.getMemberPassword()) // 필요 시 비밀번호 포함
                .memberName(request.getMemberName())
                .memberAge(request.getMemberAge())
                .memberPhone(request.getMemberPhone())
                .memberAddress(request.getMemberAddress())
                .memberBirth(request.getMemberBirth())
                .memberFlag(request.getMemberFlag())
                .memberDormantStatus(request.getMemberDormantStatus())
                .createdAt(request.getCreatedAt()) // 보통 생성 시점은 VO에서 넘어오지 않을 수 있으므로 확인 필요
                .updatedAt(LocalDateTime.now()) // 업데이트 시간은 현재 시간으로 설정
                .build();
    }

    public MemberDTO fromRequestFindCampaignStudentVOToMemberDTO(RequestFindCampaignStudentVO request) {
        return MemberDTO.builder()
                .memberCode(request.getMemberCode())
                .memberType(request.getMemberType())
                .memberEmail(request.getMemberEmail())
//                .memberPassword(request.getMemberPassword()) // 필요 시 비밀번호 포함
                .memberName(request.getMemberName())
                .memberAge(request.getMemberAge())
                .memberPhone(request.getMemberPhone())
                .memberAddress(request.getMemberAddress())
                .memberBirth(request.getMemberBirth())
                .memberFlag(request.getMemberFlag())
                .memberDormantStatus(request.getMemberDormantStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public MemberDTO fromRequestEditCampaignStudentVOToMemberDTO(RequestEditCampaignStudentVO request) {
        return MemberDTO.builder()
                .memberCode(request.getMemberCode())
                .memberType(request.getMemberType())
                .memberEmail(request.getMemberEmail())
//                .memberPassword(request.getMemberPassword()) // 필요 시 비밀번호 포함
                .memberName(request.getMemberName())
                .memberAge(request.getMemberAge())
                .memberPhone(request.getMemberPhone())
                .memberAddress(request.getMemberAddress())
                .memberBirth(request.getMemberBirth())
                .memberFlag(request.getMemberFlag())
                .memberDormantStatus(request.getMemberDormantStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
