package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonCodeDto {
    @NotBlank(message = "validation.groupCode.blank")
    private final String groupCode;

    @NotBlank(message = "validation.code.blank")
    private final String code;
}
