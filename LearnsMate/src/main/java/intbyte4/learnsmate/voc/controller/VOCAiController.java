package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.voc.domain.VOCAiAnswer;
import intbyte4.learnsmate.voc.service.VOCAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voc/ai")
@RequiredArgsConstructor
public class VOCAiController {

    private final VOCAiService vocAiService;

    @GetMapping("/current-week")
    public ResponseEntity<List<VOCAiAnswer>> getCurrentWeekAnalysis() {
        LocalDate currentWeekMonday = vocAiService.getCurrentWeekMonday();
        List<VOCAiAnswer> analysisData = vocAiService.getWeeklyAnalysis(currentWeekMonday);

        if (analysisData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(analysisData);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<VOCAiAnswer>> getAnalysisByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VOCAiAnswer> analysisData = vocAiService.getAnalysisByDate(date);

        if (analysisData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(analysisData);
    }
}
