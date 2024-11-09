package intbyte4.learnsmate.contractprocess.repository;

import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractProcessRepository extends JpaRepository<ContractProcess, Long> {

    Optional<ContractProcess> findByLectureCode(Long lectureCode);
}