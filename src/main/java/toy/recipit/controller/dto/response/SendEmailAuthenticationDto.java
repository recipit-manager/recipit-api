package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SendEmailAuthenticationDto {
    private final boolean sendEmailResult;
    private final LocalDateTime postDatetime;
}
