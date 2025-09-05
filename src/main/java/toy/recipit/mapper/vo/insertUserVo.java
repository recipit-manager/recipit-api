package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import toy.recipit.common.Constants;

@Getter
@RequiredArgsConstructor
public class insertUserVo {
    final private Long userNo;
    final private String emailHash;
    final private String emailEncrypt;
    final private String password;
    final private String firstName;
    final private String middleName;
    final private String lastName;
    final private String nickName;
    final private String countryCode;
    final private String phoneNumberHash;
    final private String phoneNumberEncrypt;
    final private int loginFailCount = Constants.UserLogin.LOGIN_FAIL_COUNT_INITIAL;
    final private String statusCode = Constants.UserStatus.ACTIVE;
}
