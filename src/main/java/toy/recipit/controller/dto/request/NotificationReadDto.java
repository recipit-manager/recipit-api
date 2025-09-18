package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class NotificationReadDto {
    @NotEmpty(message = "notice.emptyIdList")
    private final List<String> notificationIdList;
}
