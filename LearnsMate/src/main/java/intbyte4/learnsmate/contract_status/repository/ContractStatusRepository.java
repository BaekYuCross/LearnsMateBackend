package intbyte4.learnsmate.contract_status.repository;

import intbyte4.learnsmate.contract_status.domain.entity.ContractStatus;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatus, Long> {

    List<ContractStatus> findByLecture(Lecture lecture);

    Optional<ContractStatus> findByLectureAndApprovalStatus(Lecture lecture, Integer approvalStatus);

    int countByLecture(Lecture lecture);
}