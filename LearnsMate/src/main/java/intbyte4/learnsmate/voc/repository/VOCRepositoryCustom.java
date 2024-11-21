package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VOCRepositoryCustom {
    List<VOC> searchBy(VOCFilterRequestDTO dto);

    Page<VOC> searchByWithPaging(VOCFilterRequestDTO dto, Pageable pageable);
}
