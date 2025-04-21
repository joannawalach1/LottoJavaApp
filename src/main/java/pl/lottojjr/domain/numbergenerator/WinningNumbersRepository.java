package pl.lottojjr.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    Optional<WinningNumbers> findWinningNumbersByNextDrawDate(LocalDateTime lottoDrawDate);

    List<WinningNumbers> findWinningNumbersByDrawDate(LocalDateTime drawDate);
}
