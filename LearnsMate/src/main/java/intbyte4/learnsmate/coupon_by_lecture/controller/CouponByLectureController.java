package intbyte4.learnsmate.coupon_by_lecture.controller;

import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.coupon_by_lecture.domain.vo.response.CouponByLectureFindResponseVO;
import intbyte4.learnsmate.coupon_by_lecture.mapper.CouponByLectureMapper;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("couponByLectureController")
@RequestMapping("coupon-by-lecture")
@Slf4j
@RequiredArgsConstructor
public class CouponByLectureController {

    private final CouponByLectureService couponByLectureService;
    private final CouponByLectureMapper couponByLectureMapper;

    @Operation(summary = "강사 - 강의 별 쿠폰 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<CouponByLectureFindResponseVO>> findCouponByLecture(@RequestParam("tutor_code") Long tutorCode) {
        List<CouponByLectureDTO> couponByLectures = couponByLectureService.findCouponByLecture(tutorCode);
        List<CouponByLectureFindResponseVO> responseList = couponByLectures.stream()
                .map(couponByLectureMapper::fromDtoToFindResponseVO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

}
