package intbyte4.learnsmate.report.controller;

import intbyte4.learnsmate.report.service.ReposrtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReposrtService reposrtService;

    @Autowired
    public ReportController(ReposrtService reposrtService) {
        this.reposrtService = reposrtService;
    }
}
