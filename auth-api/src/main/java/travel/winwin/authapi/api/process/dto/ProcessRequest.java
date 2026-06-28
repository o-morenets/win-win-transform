package travel.winwin.authapi.api.process.dto;

import jakarta.validation.constraints.NotBlank;

public record ProcessRequest(

        @NotBlank
        String text
) {
}