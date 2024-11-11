package intbyte4.learnsmate.login_history.mapper;

import intbyte4.learnsmate.login_history.domain.dto.LoginHistoryDTO;
import intbyte4.learnsmate.login_history.domain.entity.LoginHistory;
import intbyte4.learnsmate.login_history.domain.vo.request.RequestSaveLoginHistoryVO;
import intbyte4.learnsmate.login_history.domain.vo.response.ResponseFindLoginHistoryVO;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginHistoryMapper {

    // 리스트
    public List<LoginHistoryDTO> fromLoginHistoryToLoginHistoryDTO(List<LoginHistory> loginHistoryList) {
        return loginHistoryList.stream()
                .map(entity -> LoginHistoryDTO.builder()
                        .loginHistoryCode(entity.getLoginHistoryCode())
                        .lastLoginDate(entity.getLastLoginDate())
                        .lastLogoutDate(entity.getLastLogoutDate())
                        .memberCode(entity.getMember().getMemberCode())
                        .build())
                .collect(Collectors.toList());
    }

    // 1개
    public LoginHistoryDTO fromLoginHistoryToLoginHistoryDTO(LoginHistory loginHistory) {
        return LoginHistoryDTO.builder()
                .loginHistoryCode(loginHistory.getLoginHistoryCode())
                .lastLoginDate(loginHistory.getLastLoginDate())
                .lastLogoutDate(loginHistory.getLastLogoutDate())
                .memberCode(loginHistory.getMember().getMemberCode())
                .build();
    }

    // 리스트
    public List<ResponseFindLoginHistoryVO> fromLoginHistoryDTOToResponseFindLoginHistoryVO(List<LoginHistoryDTO> loginHistoryDTOList) {
        return loginHistoryDTOList.stream()
                .map(dto -> ResponseFindLoginHistoryVO.builder()
                        .loginHistoryCode(dto.getLoginHistoryCode())
                        .lastLoginDate(dto.getLastLoginDate())
                        .lastLogoutDate(dto.getLastLogoutDate())
                        .memberCode(dto.getMemberCode())
                        .build())
                .collect(Collectors.toList());
    }

    // 1개
    public ResponseFindLoginHistoryVO fromLoginHistoryDTOToResponseFindLoginHistoryVO(LoginHistoryDTO loginHistoryDTO) {
        return ResponseFindLoginHistoryVO.builder()
                .loginHistoryCode(loginHistoryDTO.getLoginHistoryCode())
                .lastLoginDate(loginHistoryDTO.getLastLoginDate())
                .lastLogoutDate(loginHistoryDTO.getLastLogoutDate())
                .memberCode(loginHistoryDTO.getMemberCode())
                .build();
    }

    public LoginHistoryDTO fromRequestSaveLoginHistoryVOtoLoginHistoryDTO(RequestSaveLoginHistoryVO request) {
        return LoginHistoryDTO.builder()
                .lastLoginDate(request.getLastLoginDate())
                .memberCode(request.getMemberCode())
                .build();
    }

    public LoginHistory fromLoginHistoryDTOtoLoginHistory(LoginHistoryDTO dto, Member member) {
        return LoginHistory.builder()
                .lastLoginDate(dto.getLastLoginDate())
                .member(member)
                .build();
    }
}
