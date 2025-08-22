package toy.recipit.service;

import org.springframework.stereotype.Service;
import toy.recipit.mapper.UserMapper;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public boolean isNicknameDuplicate(String nickname) {
        int count = userMapper.countByNickname(nickname);

        return count > 0;
    }
}
