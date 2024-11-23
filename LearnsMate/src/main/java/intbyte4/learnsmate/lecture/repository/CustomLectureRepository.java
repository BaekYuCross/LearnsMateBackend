package intbyte4.learnsmate.lecture.repository;

import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestLectureFilterVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomLectureRepository {
    List<Lecture> findAllByFilter(LectureFilterDTO dto);

    Page<ResponseFindLectureVO> searchByWithPaging(LectureFilterDTO filterDTO, PageRequest pageable);
}
