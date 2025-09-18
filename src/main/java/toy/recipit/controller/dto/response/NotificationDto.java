package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationDto {
    private final String id;
    private final String contents;
    private final CommonCodeAndNameDto NotificationType;
    private final String readYn;
    private final String receivedTime;
}
