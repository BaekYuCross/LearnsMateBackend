package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface VOCRepositoryCustom {
    Page<VOC> searchByWithPaging(VOCFilterRequestDTO dto, Pageable pageable);

    List<VOC> findAllByFilter(VOCFilterRequestDTO dto);

    // 필터링x 정렬o
    Page<VOC> findAllBeforeNowWithSort(LocalDateTime now, Pageable pageable);

    // 필터링o 정렬o
    Page<VOC> searchByWithPagingWithSort(VOCFilterRequestDTO dto, Pageable pageable);
}
