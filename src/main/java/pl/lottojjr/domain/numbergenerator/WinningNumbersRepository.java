package pl.lottojjr.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    List<WinningNumbers> findWinningNumbersByDrawDate(LocalDateTime drawDate);
}
