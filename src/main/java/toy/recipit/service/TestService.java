package toy.recipit.service;

import org.springframework.stereotype.Service;
import toy.recipit.mapper.TestMapper;

@Service
public class TestService {
    private final TestMapper testMapper;

    public TestService(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public String test(){
        return testMapper.test();
    }
}
