package pl.lottojjr.domain.resultannouncer;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponseRepository {
    ResultResponse save(ResultResponse resultResponse);
    boolean existsById(String hash);
    Optional<ResultResponse> findById(String hash);
}
