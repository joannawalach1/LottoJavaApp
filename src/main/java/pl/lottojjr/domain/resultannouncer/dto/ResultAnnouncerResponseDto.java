package pl.lottojjr.domain.resultannouncer.dto;

public record ResultAnnouncerResponseDto(ResultResponseDto resultResponseDto,
                                         String hashDoesNotExistMessage) {

}
