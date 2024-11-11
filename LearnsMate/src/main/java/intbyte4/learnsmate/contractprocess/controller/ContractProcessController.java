package intbyte4.learnsmate.contractprocess.controller;

import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.vo.request.ResponseRegisterContractProcessVO;
import intbyte4.learnsmate.contractprocess.domain.vo.response.RequestRegisterContractProcessVO;
import intbyte4.learnsmate.contractprocess.domain.vo.response.ResponseFindContractProcessVO;
import intbyte4.learnsmate.contractprocess.mapper.ContractProcessMapper;
import intbyte4.learnsmate.contractprocess.service.ContractProcessService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contractprocess")
@Slf4j
@RequiredArgsConstructor
public class ContractProcessController {

    private final ContractProcessService contractProcessService;
    private final ContractProcessMapper contractProcessMapper;

    @Operation(summary = "계약과정코드로 계약과정 단건 조회")
    @GetMapping("/{contractProcessCode}")
    public ResponseEntity<ResponseFindContractProcessVO> getContractProcess(@PathVariable("contractProcessCode") Long contractProcessCode) {
        ContractProcessDTO contractProcessDTO = contractProcessService.getContractProcess(contractProcessCode);
        return ResponseEntity.status(HttpStatus.OK).body(contractProcessMapper.fromDtoToResponseVO(contractProcessDTO));
    }

    @Operation(summary = "강의별 계약과정 조회")
    @GetMapping("/lecture/{lectureCode}")
    public ResponseEntity<ResponseFindContractProcessVO> getApprovalProcessByLectureCode(
            @PathVariable("lectureCode") Long lectureCode) {
        ContractProcessDTO contractProcessDTO = contractProcessService.getApprovalProcessByLectureCode(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(contractProcessMapper.fromDtoToResponseVO(contractProcessDTO));
    }


    @Operation(summary = "강의별 계약과정 등록")
    @PutMapping("/lecture/{lectureCode}")
    public ResponseEntity<ResponseRegisterContractProcessVO> createContractProcessByLecture(
            @PathVariable("lectureCode") Long lectureCode, @RequestBody RequestRegisterContractProcessVO requestVO) {
        ContractProcessDTO contractProcessDTO = contractProcessService.createContractProcess(lectureCode,contractProcessMapper.fromRegisterRequestVOtoDto(requestVO));
        return  ResponseEntity.status(HttpStatus.CREATED).body(contractProcessMapper.fromDtoToRegisterResponseVO(contractProcessDTO));
    }

}
