package travel.winwin.dataapi.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TransformRequest(

        @NotBlank
        String text
) {
}