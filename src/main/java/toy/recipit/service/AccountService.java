package toy.recipit.service;

import org.springframework.stereotype.Service;
import toy.recipit.mapper.AccountMapper;

@Service
public class AccountService {
    private final AccountMapper accountMapper;

    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public boolean isNicknameDuplicate(String nickname) {
        long count = accountMapper.countByNickname(nickname);
        return count > 0;
    }
}
