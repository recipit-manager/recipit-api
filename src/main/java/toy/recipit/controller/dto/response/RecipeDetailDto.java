package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "레시피 정보")
public class RecipeDetailDto {
    @Schema(description = "작성자 닉네임", example = "우비빅")
    private final String nickname;
    @Schema(description = "레시피 번호", example = "1001")
    private final String recipeNo;
    @Schema(description = "레시피 제목", example = "김치지개")
    private final String title;
    @Schema(description = "레시피 소개", example = "맛있는 김치찌개")
    private final String description;
    @Schema(description = "레시피 조리 시간", example = "15")
    private final Integer cookingTime;
    @Schema(description = "레시피 기준량", example = "3")
    private final Integer servingSize;
    @Schema(description = "난이도", example = "쉬움")
    private final String difficulty;
    @Schema(description = "대표 이미지 url", example = "https://ik.imagekit.io/ubibik/17")
    private final String mainImageUrl;
    @Schema(description = "완성 이미지 url 배열")
    private final List<String> completionImageUrlList;
    @Schema(description = "재료 정보 목록")
    private final List<IngredientDto> ingredientList;
    @Schema(description = "조리 단계 정보 목록")
    private final List<StepDto> stepList;
    @Schema(description = "받은 좋아요 수", example = "10")
    private final Integer likeCount;
    @Schema(description = "조회한 사용자의 레시피 좋아요 여부", example = "true")
    private final String likeYn;
    @Schema(description = "조회한 사용자의 레시피 즐겨찾기 여부", example = "true")
    private final String bookmarkYn;
    @Schema(description = "조회한 사용자의 레시피 신고 여부", example = "true")
    private final String reportYn;
    @Schema(description = "레시피 상태코드", example = "RS1")
    private final String statusCode;
    @Schema(description = "레시피 상태코드명", example = "공개")
    private final String statusName;
}
