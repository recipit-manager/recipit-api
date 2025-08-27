package toy.recipit.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommonCodeItemDto {
    private final String categoryCode;
    private final String categoryCodeName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String note1;
}
