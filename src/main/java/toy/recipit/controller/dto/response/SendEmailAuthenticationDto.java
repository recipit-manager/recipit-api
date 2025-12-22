package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Schema(description = "이메일 인증코드 전송 정보")
public class SendEmailAuthenticationDto {
    @Schema(description = "이메일 인증코드 전송 결과")
    private final boolean sendEmailResult;
    @Schema(description = "이메일 인증코드 전송 시간")
    private final LocalDateTime postDatetime;
}
