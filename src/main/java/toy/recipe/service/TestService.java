package toy.recipe.service;

import org.springframework.stereotype.Service;
import toy.recipe.mapper.TestMapper;

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
