package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "작성한 레시피 정보")
public class UserRecipeDto {
    @Schema(description = "레시피 번호", example = "1001")
    private final String recipeNo;
    @Schema(description = "레시피 제목", example = "김치지개")
    private final String name;
    @Schema(description = "레시피 소개", example = "맛있는 김치찌개")
    private final String description;
    @Schema(description = "대표 이미지 url", example = "https://ik.imagekit.io/ubibik/17")
    private final String imageUrl;
    @Schema(description = "레시피 조리 시간", example = "15")
    private final int cookingTime;
    @Schema(description = "난이도", example = "쉬움")
    private final String difficulty;
    @Schema(description = "받은 좋아요 수", example = "10")
    private final int likeCount;
    @Schema(description = "사용자의 레시피 좋아요 여부", example = "true")
    private final Boolean isLiked;
}
