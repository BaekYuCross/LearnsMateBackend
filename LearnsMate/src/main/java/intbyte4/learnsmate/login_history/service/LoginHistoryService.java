package intbyte4.learnsmate.login_history.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.login_history.domain.dto.LoginHistoryDTO;
import intbyte4.learnsmate.login_history.domain.entity.LoginHistory;
import intbyte4.learnsmate.login_history.mapper.LoginHistoryMapper;
import intbyte4.learnsmate.login_history.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    // 모든 로그인 내역 조회
    public List<LoginHistoryDTO> findAllLoginHistory() {
        List<LoginHistory> LoginHistoryList = loginHistoryRepository.findAll();

        // List를 통째로 넘겨주기
        return loginHistoryMapper
                .fromLoginHistoryToLoginHistoryDTO(LoginHistoryList);
    }

    // 1개의 로그인 내역 조회
    public LoginHistoryDTO findById(Long loginHistoryCode) {
        LoginHistory loginHistory = loginHistoryRepository.findById(loginHistoryCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LOGIN_HISTORY_NOT_FOUND));

        return loginHistoryMapper.fromLoginHistoryToLoginHistoryDTO(loginHistory);
    }


}
