package travel.winwin.dataapi.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.dataapi.api.dto.TransformRequest;
import travel.winwin.dataapi.api.dto.TransformResponse;

@RestController
@RequestMapping("/api")
public class TransformController {

    @PostMapping("/transform")
    public TransformResponse transform(@Valid @RequestBody TransformRequest request) {
        String result = new StringBuilder(request.text())
                .reverse()
                .toString()
                .toUpperCase();
        return new TransformResponse(result);
    }
}