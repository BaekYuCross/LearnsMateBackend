package intbyte4.learnsmate.login_history.controller;

import intbyte4.learnsmate.login_history.mapper.LoginHistoryMapper;
import intbyte4.learnsmate.login_history.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;
    private final LoginHistoryMapper loginHistoryMapper;
}
