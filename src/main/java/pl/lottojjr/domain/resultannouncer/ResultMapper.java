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

    public static ResultResponseDto mapToEntity(ResultResponseDto resultResponseDto) {
        return ResultResponseDto.builder()
                .hash(resultResponseDto.hash())
                .userNumbers(resultResponseDto.userNumbers())
                .hitNumbers(resultResponseDto.hitNumbers())
                .drawDate(resultResponseDto.drawDate())
                .isWinner(resultResponseDto.isWinner())
                .build();
    }
}
