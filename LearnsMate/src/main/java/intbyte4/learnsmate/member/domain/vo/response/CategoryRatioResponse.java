package intbyte4.learnsmate.member.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CategoryRatioResponse {
    private Map<String, Long> categoryCounts;
}