package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutoCompleteDto {
    @NotBlank(message = "refriItem.keyword.blank")
    private final String keyword;
}
