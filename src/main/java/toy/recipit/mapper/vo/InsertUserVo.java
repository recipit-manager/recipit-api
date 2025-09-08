package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.request.SignUpDto;

@Getter
@RequiredArgsConstructor
public class InsertUserVo {
    final private String emailHashing;
    final private String emailEncrypt;
    final private String password;
    final private String firstName;
    final private String middleName;
    final private String lastName;
    final private String nickName;
    final private String countryCode;
    final private String phoneNumberHashing;
    final private String phoneNumberEncrypt;
    final private int loginFailCount = Constants.UserLogin.LOGIN_FAIL_COUNT_INITIAL;
    final private String statusCode = Constants.UserStatus.ACTIVE;

    public InsertUserVo(SignUpDto signUpDto,
                        String emailHashing,
                        String emailEncrypt,
                        String encodedPassword,
                        String phoneNumberHashing,
                        String phoneNumberEncrypt) {
        this.emailHashing = emailHashing;
        this.emailEncrypt = emailEncrypt;
        this.password = encodedPassword;
        this.firstName = signUpDto.getFirstName();
        this.middleName = signUpDto.getMiddleName();
        this.lastName = signUpDto.getLastName();
        this.nickName = signUpDto.getNickname();
        this.countryCode = signUpDto.getCountryCode().getCode();
        this.phoneNumberHashing = phoneNumberHashing;
        this.phoneNumberEncrypt = phoneNumberEncrypt;
    }
}
