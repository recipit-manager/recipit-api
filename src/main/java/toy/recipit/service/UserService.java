package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public boolean isNicknameDuplicate(String nickname) {

        return userMapper.isNicknameDuplicate(nickname);
    }
}
