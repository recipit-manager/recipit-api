package toy.recipit.mapper.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserVo {
    private Long userNo;
    private String email;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickName;
    private String countryCode;
    private String phoneNumber;
    private int loginFailCount;
    private String statusCode;
}
