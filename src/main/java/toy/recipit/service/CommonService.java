package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.controller.dto.CountryCode;
import toy.recipit.mapper.CommonMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonMapper commonMapper;

    public List<CountryCode> getCountryCodes(String language) {
        String groupCode;

        switch (language) {
            case "ko" -> groupCode = "CT100";
            case "en" -> groupCode = "CT200";
            default -> throw new IllegalArgumentException("잘못된 국가입니다 -  " + language);
        }

        return commonMapper.getCountryCodes(groupCode);
    }
}
