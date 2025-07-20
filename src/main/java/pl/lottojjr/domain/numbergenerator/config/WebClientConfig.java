package pl.lottojjr.domain.numbergenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://www.randomnumberapi.com/api/v1.0/random?min=1&max=49&count=6")
                .build();
    }
}