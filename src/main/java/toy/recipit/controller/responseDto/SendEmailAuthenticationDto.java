package toy.recipit.controller.responseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@RequiredArgsConstructor
public class SendEmailAuthenticationDto {
    private final boolean sendEmailResult;
    private final OffsetDateTime postDatetime;
}
