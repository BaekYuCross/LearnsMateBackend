package intbyte4.learnsmate.contractprocess.controller;

import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.vo.ResponseContractProcessVO;
import intbyte4.learnsmate.contractprocess.mapper.ContractProcessMapper;
import intbyte4.learnsmate.contractprocess.service.ContractProcessService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contractprocess")
@Slf4j
@RequiredArgsConstructor
public class ContractProcessController {

    private final ContractProcessService contractProcessService;
    private final ContractProcessMapper contractProcessMapper;

    @Operation(summary = "계약과정코드로 계약과정 단건 조회")
    @GetMapping("/{contractProcessCode}")
    public ResponseEntity<ResponseContractProcessVO> getContractProcess(@PathVariable("contractProcessCode") Long contractProcessCode) {
        ContractProcessDTO contractProcessDTO = contractProcessService.getContractProcess(contractProcessCode);
        return ResponseEntity.status(HttpStatus.OK).body(contractProcessMapper.fromDtoToResponseVO(contractProcessDTO));
    }

    @Operation(summary = "강사별 강의별 계약과정 조회")
    @GetMapping("/tutor/{tutorCode}")
    public ResponseEntity<List<ResponseContractProcessVO>> getApprovalProcessByLectureCode(@PathVariable("tutorCode") Long tutorCode) {
        List<ContractProcessDTO> contractProcessDTOs = contractProcessService.getApprovalProcessByLectureCode(tutorCode);

        List<ResponseContractProcessVO> responseVOs = contractProcessDTOs.stream()
                .map(contractProcessMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseVOs);
    }
}
