package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "입력한 닉네임 정보")
public class ChangeNicknameDto {
    @NotBlank(message = "validation.nickname.blank")
    @Size(min = 2, max = 8, message = "validation.nickname.size")
    @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
    @Schema(description = "변경할 닉네임", example = "우비빅")
    private final String nickname;
}
