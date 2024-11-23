package intbyte4.learnsmate.lecture.domain.vo.response;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LecturePageInfo {
    private List<LectureDTO> lectures;
    private long totalElements;
}