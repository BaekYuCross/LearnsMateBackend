package intbyte4.learnsmate.contractprocess.repository;

import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContractProcessRepository extends JpaRepository<ContractProcess, Long> {

    ContractProcess findByLecture(Lecture lecture);

    Optional<ContractProcess> findByLectureAndApprovalProcess(Lecture lecture, Integer approvalProcess);

    long countByLecture(Lecture lecture);
}