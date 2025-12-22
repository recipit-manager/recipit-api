package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "조리 단계 정보")
public class StepDto {
    @Schema(description = "조리 단계 내용", example = "고기를 구워줍니다.")
    private final String content;
    @Schema(description = "단계 이미지 url 목록")
    private final List<String> imageUrlList;
}
