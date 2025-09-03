package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserEmailVerification {
    final private String email;
    final private String verifyingCode;
    final private String verifyingStatusCode;
    final private LocalDateTime verifyingExpireDatetime;
}
