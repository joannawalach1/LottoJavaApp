package pl.lottojjr.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lottojjr.domain.numberreceiver.Ticket;
import pl.lottojjr.domain.resultchecker.dto.ResultDto;

@Repository
public interface PlayerRepository<P, S> extends MongoRepository<Player, String> {
    <S2 extends Ticket> S2 save(S2 entity);

    ResultDto findByHash(String hash);
}
