package pl.lottojjr.domain.resultannouncer;

import lombok.RequiredArgsConstructor;
import pl.lottojjr.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lottojjr.domain.resultannouncer.dto.ResultResponseDto;
import pl.lottojjr.domain.resultchecker.ResultCheckerFacade;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static pl.lottojjr.domain.resultannouncer.MessageResponse.*;

@RequiredArgsConstructor
public class ResultAnnouncerFacade {
    private static final LocalTime RESULT_ANNOUNCEMENT_TIME = LocalTime.of(12, 0).plusMinutes(5);
    private final ResultCheckerFacade resultCheckerFacade;
    private final Clock clock;
    private final ResponseRepository responseRepository;

    public ResultAnnouncerResponseDto checkResult(String hash) {
        Optional<ResultResponse> cached = responseRepository.findById(hash);
        if (cached.isPresent()) {
            return new ResultAnnouncerResponseDto(
                    ResultMapper.mapToDto(cached.get()),
                    ALREADY_CHECKED.info
            );
        }

        ResultDto resultDto = resultCheckerFacade.findResultByHash(hash);
        if (resultDto == null) {
            return new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        }

        ResultResponseDto responseDto = buildResponseDto(resultDto);
        responseRepository.save(buildResponse(responseDto));

        if (!isAfterResultAnnouncementTime(resultDto)) {
            return new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        }
        if (resultDto.isWinner()) {
            return new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        }
        return new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
    }

    private static ResultResponse buildResponse(ResultResponseDto responseDto) {
        return ResultResponse.builder()
                .hash(responseDto.hash())
                .userNumbers(responseDto.userNumbers())
                .hitNumbers(responseDto.hitNumbers())
                .drawDate(responseDto.drawDate())
                .isWinner(responseDto.isWinner())
                .build();
    }

    private static ResultResponseDto buildResponseDto(ResultDto resultResponse) {
        return ResultResponseDto.builder()
                .hash(resultResponse.hash())
                .userNumbers(resultResponse.numbers())
                .hitNumbers(resultResponse.hitNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultResponseDto) {
        LocalDateTime announcementDateTime = LocalDateTime.of(resultResponseDto.drawDate().toLocalDate(), RESULT_ANNOUNCEMENT_TIME);
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}