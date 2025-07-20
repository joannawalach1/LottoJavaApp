package pl.lottojjr.domain.numbergenerator;

import lombok.RequiredArgsConstructor;
import pl.lottojjr.infrastructure.numbergenerator.WebClientFetcher;

import java.util.Set;

@RequiredArgsConstructor
public class NumberGenerator {
    private final WebClientFetcher webClientFetcher;

    public Set<Integer> generateRandomNumbers() {
        return webClientFetcher.fetchRandomNumbers();
    }
}
