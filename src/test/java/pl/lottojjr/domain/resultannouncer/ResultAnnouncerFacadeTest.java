package pl.lottojjr.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lottojjr.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lottojjr.domain.resultannouncer.dto.ResultResponseDto;
import pl.lottojjr.domain.resultchecker.ResultCheckerFacade;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lottojjr.domain.resultannouncer.MessageResponse.*;

class ResultAnnouncerFacadeTest {
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void it_should_return_response_with_lose_message_if_ticket_is_not_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, Clock.systemUTC(), responseRepository);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findResultByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto responseDto = ResultResponseDto.builder()
                .hash("123")
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void it_should_return_response_with_win_message_if_ticket_is_winning_ticket() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, Clock.systemUTC(), responseRepository);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findResultByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto resultResponseDto = ResultResponseDto.builder()
                .hash("123")
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(resultResponseDto, WIN_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void it_should_return_response_with_wait_message_if_date_is_before_announcement_time() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 31, 12, 0, 0);
        String hash = "123";
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 17, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, clock, responseRepository);
        ResultDto resultDto = ResultDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findResultByHash(hash)).thenReturn(resultDto);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultResponseDto responseDto = ResultResponseDto.builder()
                .hash("123")
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 9, 0))
                .drawDate(drawDate)
                .isWinner(true)
                .build();

        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void it_should_return_response_with_hash_does_not_exist_message_if_hash_does_not_exist() {
        //given
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, Clock.systemUTC(), responseRepository);

        when(resultCheckerFacade.findResultByHash(hash)).thenReturn(null);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }


    @Test
    void should_return_cached_result_with_already_checked_message_when_result_checked_twice() {
        // given
        String hash = "123";
        ResultDto generatedResult = new ResultDto(
                hash,
                Set.of(6, 1, 2, 3, 4, 5),
                Set.of(1, 2, 3),
                LocalDateTime.of(2022, 12, 17, 12, 0),
                "You win!",
                true
        );

        ResultDto cachedResponse = new ResultDto(
                hash,
                Set.of(6, 1, 2, 3, 4, 5),
                Set.of(1, 2, 3),
                LocalDateTime.of(2022, 12, 17, 12, 0),
                "You have already checked your ticket, please come back later",
                true
        );

        when(resultCheckerFacade.findResultByHash(hash)).thenReturn(cachedResponse);


        ResultAnnouncerFacade resultAnnouncerFacade =
                new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, Clock.systemUTC(), responseRepository);

        // when
        // First check - not cached yet
        ResultAnnouncerResponseDto firstCheck = resultAnnouncerFacade.checkResult(hash);

        // Second check - should return ALREADY_CHECKED
        ResultAnnouncerResponseDto secondCheck = resultAnnouncerFacade.checkResult(hash);

        // then
        assertEquals("You have already checked your ticket, please come back later", secondCheck.hashDoesNotExistMessage());
        assertNotNull(secondCheck.resultResponseDto());
        assertEquals(hash, secondCheck.resultResponseDto().hash());
        assertEquals(LocalDateTime.of(2022, 12, 17, 12, 0), secondCheck.resultResponseDto().drawDate());
        assertTrue(secondCheck.resultResponseDto().isWinner());
    }

    }
