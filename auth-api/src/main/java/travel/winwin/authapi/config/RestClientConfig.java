package travel.winwin.authapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final AppProperties properties;

    public RestClientConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestClient dataApiClient() {
        return RestClient.builder()
                .baseUrl(properties.dataApi().url())
                .defaultHeader("X-Internal-Token", properties.internalToken())
                .build();
    }
}