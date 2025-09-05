package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResultDto {
    private final boolean signUpResult;
    private final String signUpResultMessage;

}
