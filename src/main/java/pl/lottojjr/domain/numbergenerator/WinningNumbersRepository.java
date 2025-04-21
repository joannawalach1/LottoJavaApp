package pl.lottojjr.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
}
