package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchRefriVo {
    private final String id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Integer cookingTime;
    private final Integer servingSize;
    private final String difficultyCode;
    private final String difficulty;
    private final Integer likeCount;
    private final Boolean isLiked;
}