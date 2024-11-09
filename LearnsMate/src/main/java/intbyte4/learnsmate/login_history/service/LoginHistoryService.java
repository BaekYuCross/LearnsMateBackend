package intbyte4.learnsmate.login_history.service;

import intbyte4.learnsmate.login_history.mapper.LoginHistoryMapper;
import intbyte4.learnsmate.login_history.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;
}
