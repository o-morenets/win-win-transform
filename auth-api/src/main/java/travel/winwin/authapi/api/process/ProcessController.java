package travel.winwin.authapi.api.process;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import travel.winwin.authapi.api.process.dto.ProcessRequest;
import travel.winwin.authapi.api.process.dto.ProcessResponse;
import travel.winwin.authapi.domain.processing.ProcessingLog;
import travel.winwin.authapi.domain.processing.ProcessingLogRepository;
import travel.winwin.authapi.domain.user.User;
import travel.winwin.authapi.domain.user.UserRepository;
import travel.winwin.authapi.security.UserPrincipal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessController {

    private final RestClient dataApiClient;
    private final ProcessingLogRepository processingLogRepository;
    private final UserRepository userRepository;

    @PostMapping("/process")
    public ProcessResponse process(
            @Valid @RequestBody ProcessRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ProcessResponse response = dataApiClient.post()
                .uri("/api/transform")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ProcessResponse.class);

        User user = userRepository.getReferenceById(principal.getId());
        processingLogRepository.save(
                new ProcessingLog(user, request.text(), response.result())
        );

        return response;
    }
}