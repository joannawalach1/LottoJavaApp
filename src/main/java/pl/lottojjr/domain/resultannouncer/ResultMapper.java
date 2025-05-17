package pl.lottojjr.domain.resultannouncer;

import pl.lottojjr.domain.resultannouncer.dto.ResultResponseDto;

public class ResultMapper {
    static ResultResponseDto mapToDto(ResultResponse resultResponse) {
        return ResultResponseDto.builder()
                .hash(resultResponse.hash())
                .userNumbers(resultResponse.userNumbers())
                .hitNumbers(resultResponse.hitNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }
}
