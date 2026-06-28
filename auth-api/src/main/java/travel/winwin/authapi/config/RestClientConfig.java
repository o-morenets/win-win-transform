package travel.winwin.authapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final AppProperties properties;

    @Bean
    public RestClient dataApiClient() {
        return RestClient.builder()
                .baseUrl(properties.dataApi().url())
                .defaultHeader("X-Internal-Token", properties.internalToken())
                .build();
    }
}