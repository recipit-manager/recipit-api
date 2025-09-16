package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChangeTemporaryPasswordDto {
    @NotBlank(message = "validation.password.blank")
    @Size(min = 8, max = 16, message = "validation.password.size")
    @Pattern.List({
            @Pattern(
                    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).{8,16}$",
                    message = "validation.password.complexity"
            ),
            @Pattern(
                    regexp = "^(?!.*(.)\\1\\1).*$",
                    message = "validation.password.repetition"
            )
    })
    private final String password;
}
