package intbyte4.learnsmate.preferred_topics.controller;

import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicsDTO;
import intbyte4.learnsmate.preferred_topics.domain.vo.response.ResponseFindPreferredTopicsVO;
import intbyte4.learnsmate.preferred_topics.mapper.PreferredTopicsMapper;
import intbyte4.learnsmate.preferred_topics.service.PreferredTopicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topics")
public class PreferredTopicsController {

    private final PreferredTopicsService preferredTopicsService;
    private final PreferredTopicsMapper preferredTopicsMapper;

    // 1. 모든 선호주제 조회
    @GetMapping
    public ResponseEntity<List<ResponseFindPreferredTopicsVO>> findAllPreferredTopics() {

        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findAll();

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 2. 특정 멤버가 선호하는 모든 주제 조회
    @GetMapping("/{topicscode}")
    public ResponseEntity<ResponseFindPreferredTopicsVO> findPreferredTopics(@PathVariable("topicscode") Long topicsCode){
        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findById(topicsCode);

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList.get(0));
    }

    // 3. 특정 멤버가 선택한 모든 선호주제 조회
    public ResponseEntity<List<ResponseFindPreferredTopicsVO>> findAllPreferredTopicsByMemberCode(
            @PathVariable("membercode") Long memberCode) {
        List<PreferredTopicsDTO> dtoList = preferredTopicsService.findAllByMemberCode(memberCode);

        List<ResponseFindPreferredTopicsVO> voList
                = preferredTopicsMapper.fromPreferredTopicsDTOtoResponseFindPreferredTopicsVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }
}
