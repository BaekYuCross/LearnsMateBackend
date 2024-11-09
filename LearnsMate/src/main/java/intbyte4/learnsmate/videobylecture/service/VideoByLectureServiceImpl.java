package intbyte4.learnsmate.videobylecture.service;

import intbyte4.learnsmate.videobylecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.videobylecture.repository.VideoByLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoByLectureServiceImpl implements VideoByLectureService {

    private final VideoByLectureRepository videoByLectureRepository;

    // 강의의 동영상 개수 조회
    @Override
    public CountVideoByLectureDTO getVideoByLecture(Long lectureCode) {
        long videoCount = videoByLectureRepository.countByLectureCode(lectureCode);
        return new CountVideoByLectureDTO(lectureCode, videoCount);
    }

}
