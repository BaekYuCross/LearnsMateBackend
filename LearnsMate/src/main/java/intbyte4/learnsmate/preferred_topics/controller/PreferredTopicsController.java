package intbyte4.learnsmate.preferred_topics.controller;

import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.vo.request.RequestSavePreferredHistoryVO;
import intbyte4.learnsmate.preferred_topics.domain.vo.response.ResponseFindPreferredTopicsVO;
import intbyte4.learnsmate.preferred_topics.mapper.PreferredTopicsMapper;
import intbyte4.learnsmate.preferred_topics.service.PreferredTopicsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topics")
public class PreferredTopicsController {

    private final PreferredTopicsService preferredTopicsService;
    private final PreferredTopicsMapper preferredTopicsMapper;

    // 1. 모든 선호주제 조회
    @Operation(summary = "직원 - 모든 선호주제 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindPreferredTopicsVO>> findAllPreferredTopics() {

        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findAll();

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 2. 특정 선호주제 조회
    @Operation(summary = "직원 - 특정 선호주제 조회")
    @GetMapping("/{topicscode}")
    public ResponseEntity<ResponseFindPreferredTopicsVO> findPreferredTopics(@PathVariable("topicscode") Long topicsCode){
        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findById(topicsCode);

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList.get(0));
    }

    // 3. 특정 멤버가 선택한 모든 선호주제 조회
    @Operation(summary = "특정 멤버가 선택한 모든 선호주제 조회")
    @GetMapping("/member/{membercode}")
    public ResponseEntity<List<ResponseFindPreferredTopicsVO>> findAllPreferredTopicsByMemberCode(
            @PathVariable("membercode") Long memberCode) {
        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findAllByMemberCode(memberCode);

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 4. 특정 멤버 선호 주제 등록
    @Operation(summary = "특정 멤버의 선호주제 리스트 등록")
    @PostMapping("/member")
    public ResponseEntity<String> savePreferredTopics(@RequestBody RequestSavePreferredHistoryVO request) {
        List<PreferredTopicsDTO> dtoList
                = preferredTopicsMapper.fromRequestSavePreferredHistoryVOtoPreferredTopicsDTOList(request);

        preferredTopicsService.savePreferredTopics(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body("선호 주제 등록 성공");
    }
}
