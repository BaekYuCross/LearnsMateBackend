package intbyte4.learnsmate.contractprocess.repository;

import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractProcessRepository extends JpaRepository<ContractProcess, Long> {

    List<ContractProcessDTO> findByLecture_LectureCodeIn(List<Long> lectureCodes);
}