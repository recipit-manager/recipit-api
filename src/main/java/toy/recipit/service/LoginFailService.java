package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.UserVo;

@Service
@RequiredArgsConstructor
public class LoginFailService {
    private final UserMapper userMapper;
    private final int LOGIN_FAIL_INACTIVE_THRESHOLD = 5;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notMatchPassword(UserVo userVo) {
        userMapper.increaseLoginFailCount(userVo.getEmailHashing(), Constants.SystemId.SYSTEM_NUMBER);

        if (userVo.getLoginFailCount() + 1 >= LOGIN_FAIL_INACTIVE_THRESHOLD) {
            userMapper.updateStatusCode(userVo.getEmailHashing(), Constants.UserStatus.INACTIVE, Constants.SystemId.SYSTEM_NUMBER);
        }
    }
}
