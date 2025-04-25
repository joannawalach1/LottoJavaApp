package pl.lottojjr.infrastructure.numbergenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WebClientFetcher {
    private final WebClient webClient;

    public Set<Integer> fetchRandomNumbers() {
        Set<Integer> numbers = webClient.get()
                .uri("https://www.randomnumberapi.com/api/v1.0/random?min=1&max=49&count=6")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<Integer>>() {})
                .block();
        return numbers.stream().collect(Collectors.toSet());
    }
}
