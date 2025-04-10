package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.campaign.domain.dto.FindCampaignDetailDTO;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import intbyte4.learnsmate.member.domain.pagination.MemberPageResponse;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    void saveMember(MemberDTO memberDTO);

    // 회원 로그인
    void loginMember(MemberDTO memberDTO);

    MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberType(int page, int size, MemberType memberType);

    MemberDTO findByStudentCode(Long memberCode);

    // 멤버 회원정보 수정 메서드
    void editMember(MemberDTO memberDTO);

    // 멤버 회원 삭제 flag false 메서드
    void deleteMember(Long memberCode);

    // memberCode, memberType -> memberDTO로 반환 메서드
    MemberDTO findMemberByMemberCode(Long memberCode, MemberType memberType);

    // 멤버 타입과 상관 없이 멤버 코드로 조회하는 메서드
    MemberDTO findById(Long memberCode);

    // 캠페인 코드로 멤버를 조회하는 메서드 (Campaign에서 사용)
    Page<MemberDTO> findMembersByCampaignCode(FindCampaignDetailDTO campaignDTO, Pageable pageable);

    // 학생 필터링하는 서비스 코드
    MemberPageResponse<ResponseFindMemberVO> filterStudent(MemberFilterRequestDTO dto, int page, int size);

    // 학생 필터링하는 서비스 코드 + 컬럼정렬
    MemberPageResponse<ResponseFindMemberVO> filterStudentBySort(MemberFilterRequestDTO dto, int page, int size, String sortField, String sortDirection);

    // 강사 필터링하는 코드 -> 강사 필터링은 조건이 더 적음(멤버에 다 포함됨)
    MemberPageResponse<ResponseFindMemberVO> filterTutortBySort(MemberFilterRequestDTO dto, int page, int size, String sortField, String sortDirection);

    List<MemberDTO> findAllByFilterWithExcel(MemberFilterRequestDTO filterDTO);

    List<MemberDTO> findAllByMemberTypeWithExcel(MemberType memberType);

    public int findYesterdayCountByMemberType(MemberType memberType);

    public int findTotalCountByMemberType(MemberType memberType);
}
