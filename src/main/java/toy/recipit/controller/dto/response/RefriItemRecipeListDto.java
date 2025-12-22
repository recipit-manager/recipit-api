package toy.recipit.controller.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "냉템요리 레시피 정보")
public class RefriItemRecipeListDto {
    @Schema(description = "레시피 번호", example = "1001")
    private final String id;
    @Schema(description = "레시피 제목", example = "김치지개")
    private final String name;
    @Schema(description = "레시피 소개", example = "맛있는 김치찌개입니다.")
    private final String description;
    @Schema(description = "대표 이미지 url", example = "https://ik.imagekit.io/ubibik/17")
    private final String imageUrl;
    @Schema(description = "레시피 조리 시간", example = "15")
    private final Integer cookingTime;
    @Schema(description = "레시피 기준량", example = "3")
    private final Integer servingSize;
    @Schema(description = "난이도 명", example = "쉬움")
    private final String difficulty;
    @Schema(description = "난이도 코드", example = "D1")
    private final String difficultyCode;
    @Schema(description = "좋아요 수", example = "100")
    private final Integer likeCount;
    @Schema(description = "사용자의 좋아요 여부", example = "true")
    private final Boolean isLiked;
    @Schema(description = "매칭되지 않은 재료 목록", example = "[감자, 고구마]")
    private final List<String> unMatchIngredientlist;
}