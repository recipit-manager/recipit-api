package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.CountryCodeDto;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.vo.CmDetailCodeVo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonMapper commonMapper;

    public List<CountryCodeDto> getCountryCodes(String groupCode) {
        return commonMapper.getCommonDetailCodes(groupCode).stream()
                .map(vo -> new CountryCodeDto(
                        vo.getCode(),
                        vo.getCodeName(),
                        vo.getNote4(),
                        vo.getNote2(),
                        vo.getNote3()
                ))
                .toList();
    }

    public List<String> getEmailDomains() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.EMAIL).stream()
                .map(CmDetailCodeVo::getCodeName).toList();
    }
}
