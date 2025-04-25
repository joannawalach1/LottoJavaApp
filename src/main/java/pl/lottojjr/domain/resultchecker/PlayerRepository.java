package pl.lottojjr.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> saveAll(List<Player> player);
    Optional<Player> findById(String id);
}
