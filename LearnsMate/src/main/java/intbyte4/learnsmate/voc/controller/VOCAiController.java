package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.voc.domain.VOCAiAnswer;
import intbyte4.learnsmate.voc.service.VOCAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voc/ai")
@RequiredArgsConstructor
public class VOCAiController {

    private final VOCAiService vocAiService;

    @GetMapping("/select-weekly-monday")
    public ResponseEntity<List<VOCAiAnswer>> getWeekAnalysis() {
        LocalDate currentWeekMonday = vocAiService.getCurrentWeekMonday();
        List<VOCAiAnswer> analysisData = vocAiService.getWeeklyAnalysis(currentWeekMonday);

        if (analysisData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(analysisData);
    }
}
