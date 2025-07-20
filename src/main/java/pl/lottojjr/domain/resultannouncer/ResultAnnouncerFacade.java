package pl.lottojjr.domain.resultannouncer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.lottojjr.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lottojjr.domain.resultannouncer.dto.ResultResponseDto;
import pl.lottojjr.domain.resultchecker.ResultCheckerFacade;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static pl.lottojjr.domain.resultannouncer.MessageResponse.*;

@RequiredArgsConstructor
@Log4j2
public class ResultAnnouncerFacade {
    private static final LocalTime RESULT_ANNOUNCEMENT_TIME = LocalTime.of(12, 0).plusMinutes(5);
    private final ResultCheckerFacade resultCheckerFacade;
    private final Clock clock;
    private final ResponseRepository responseRepository;

    public ResultAnnouncerResponseDto checkResult(String hash) {
        log.info("Start checkResult with hash={}", hash);

        ResultResponse cached = responseRepository.findByHash(hash);
        if (cached != null) {
            log.info("Found cached result for hash={}, returning ALREADY_CHECKED response", hash);
            return new ResultAnnouncerResponseDto(
                    ResultMapper.mapToDto(cached),
                    ALREADY_CHECKED.info
            );
        }

        ResultDto resultDto = resultCheckerFacade.findResultByHash(hash);
        log.info("ResultDto fetched by findResultByHash: {}", resultDto);

        if (resultDto == null) {
            log.warn("No result found for hash={}, returning HASH_DOES_NOT_EXIST_MESSAGE", hash);
            return new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        }

        ResultResponseDto responseDto = buildResponseDto(resultDto);
        log.info("Built ResultResponseDto: {}", responseDto);

        responseRepository.save(buildResponse(responseDto));
        log.info("Saved responseDto to repository");

        if (!isAfterResultAnnouncementTime(resultDto)) {
            log.info("Result announcement time not reached for hash={}, returning WAIT_MESSAGE", hash);
            return new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        }

        if (resultDto.isWinner()) {
            log.info("Result is winner for hash={}, returning WIN_MESSAGE", hash);
            return new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        }

        log.info("Result is not winner for hash={}, returning LOSE_MESSAGE", hash);
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