package intbyte4.learnsmate.contract_status.controller;

import intbyte4.learnsmate.contract_status.domain.dto.ContractStatusDTO;
import intbyte4.learnsmate.contract_status.domain.vo.request.ResponseRegisterContractStatusVO;
import intbyte4.learnsmate.contract_status.domain.vo.response.RequestRegisterContractStatusVO;
import intbyte4.learnsmate.contract_status.domain.vo.response.ResponseFindContractStatusVO;
import intbyte4.learnsmate.contract_status.mapper.ContractStatusMapper;
import intbyte4.learnsmate.contract_status.service.ContractStatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contract-status")
@Slf4j
@RequiredArgsConstructor
public class ContractStatusController {

    private final ContractStatusService contractStatusService;
    private final ContractStatusMapper contractStatusMapper;

    @Operation(summary = "계약과정 전체 조회")
    @GetMapping("list")
    public ResponseEntity<List<ResponseFindContractStatusVO>> listContractProcess() {
        List<ContractStatusDTO> contractStatusDTOList = contractStatusService.findAll();
        List<ResponseFindContractStatusVO> response = new ArrayList<>();
        for (ContractStatusDTO contractStatusDTO : contractStatusDTOList) {
            ResponseFindContractStatusVO responseVO = contractStatusMapper.fromDtoToResponseVO(contractStatusDTO);
            response.add(responseVO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "계약과정코드로 계약과정 단건 조회")
    @GetMapping("/{contractProcessCode}")
    public ResponseEntity<ResponseFindContractStatusVO> getContractProcess(@PathVariable("contractProcessCode") Long contractProcessCode) {
        ContractStatusDTO contractStatusDTO = contractStatusService.getContractProcess(contractProcessCode);
        return ResponseEntity.status(HttpStatus.OK).body(contractStatusMapper.fromDtoToResponseVO(contractStatusDTO));
    }

    @Operation(summary = "강의별 계약과정 조회")
    @GetMapping("/lecture/{lectureCode}")
    public ResponseEntity<ResponseFindContractStatusVO> getApprovalProcessByLectureCode(@PathVariable("lectureCode") String lectureCode) {
        ContractStatusDTO contractStatusDTO = contractStatusService.getApprovalProcessByLectureCode(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(contractStatusMapper.fromDtoToResponseVO(contractStatusDTO));
    }

    @Operation(summary = "강의별 계약과정 등록")
    @PostMapping("/lecture-approve/{lectureCode}")
    public ResponseEntity<ResponseRegisterContractStatusVO> createContractProcessByLecture(
            @PathVariable("lectureCode") String lectureCode, @RequestBody RequestRegisterContractStatusVO requestVO) {
        ContractStatusDTO contractStatusDTO = contractStatusService.createContractProcess(lectureCode, contractStatusMapper.fromRegisterRequestVOtoDto(requestVO));
        return  ResponseEntity.status(HttpStatus.CREATED).body(contractStatusMapper.fromDtoToRegisterResponseVO(contractStatusDTO));
    }
}
