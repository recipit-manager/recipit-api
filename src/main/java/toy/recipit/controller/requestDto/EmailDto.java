package toy.recipit.controller.requestDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EmailDto {

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
    private final String email;

    @JsonCreator
    public EmailDto(@JsonProperty("email") String email) {
        this.email = email;
    }
}
