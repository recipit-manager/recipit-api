package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "인기 레시피 정보")
public class PopularRecipeDto {
    @Schema(description = "레시피 번호", example = "1001")
    private final String id;
    @Schema(description = "레시피 제목", example = "김치찌개")
    private final String name;
    @Schema(description = "레시피 대표사진 url", example = "https://ik.imagekit.io/ubibik/17")
    private final String imageUrl;
    @Schema(description = "좋아요 수", example = "100")
    private final int likeCount;
    @Schema(description = "사용자의 좋아요 여부", example = "true")
    private final Boolean isLiked;
}
