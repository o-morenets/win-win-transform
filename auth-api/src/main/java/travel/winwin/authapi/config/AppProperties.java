package travel.winwin.authapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        Jwt jwt,
        String internalToken,
        DataApi dataApi
) {

    public record Jwt(
            String secret,
            @DefaultValue("86400000") long expirationMs
    ) {}

    public record DataApi(
            @DefaultValue("http://data-api:8081") String url
    ) {}
}