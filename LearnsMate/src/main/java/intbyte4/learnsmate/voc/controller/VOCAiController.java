package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.voc.domain.VOCAiAnswer;
import intbyte4.learnsmate.voc.service.VOCAiService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voc/ai")
@RequiredArgsConstructor
public class VOCAiController {

    private final VOCAiService vocAiService;

    @Operation(summary = "이번 주차 voc 분석 조회")
    @GetMapping("/current-week")
    public ResponseEntity<List<VOCAiAnswer>> getCurrentWeekAnalysis() {
        LocalDate currentWeekMonday = vocAiService.getCurrentWeekMonday();
        List<VOCAiAnswer> analysisData = vocAiService.getWeeklyAnalysis(currentWeekMonday);

        if (analysisData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(analysisData);
    }

    @Operation(summary = "voc 분석 월요일 주차 별 조회")
    @GetMapping("/by-date")
    public ResponseEntity<List<VOCAiAnswer>> getAnalysisByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<VOCAiAnswer> analysisData = vocAiService.getAnalysisByDate(date);

        if (analysisData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(analysisData);
    }

    @Operation(summary = "특정 날짜의 VOC 분석 수동 실행")
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeVocForDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            vocAiService.analyzeVocForSpecificDate(date);
            return ResponseEntity.ok("특정 날짜(" + date + ")의 VOC 분석이 성공적으로 실행되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("VOC 분석 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
