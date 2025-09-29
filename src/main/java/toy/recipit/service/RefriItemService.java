package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.mapper.RefriItemMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefriItemService {
    private final RefriItemMapper refriItemMapper;

    public List<String> getAutoCompleteList(String keyword) {
        return refriItemMapper.getAutoCompleteList(keyword);
    }
}
