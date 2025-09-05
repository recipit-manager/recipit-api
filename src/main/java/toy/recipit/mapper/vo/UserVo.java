package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserVo {
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
    final private int loginFailCount;
    final private String statusCode;
}
