package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.request.NotificationReadDto;
import toy.recipit.controller.dto.response.CommonCodeAndNameDto;
import toy.recipit.controller.dto.response.NotificationDto;
import toy.recipit.mapper.NotificationMapper;
import toy.recipit.mapper.vo.NotificationVo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationMapper notificationMapper;

    public List<NotificationDto> getNotifications(String userNo) {
        List<NotificationVo> notifications = notificationMapper.getNotifications(userNo, Constants.GroupCode.NOTIFICATION);

        return notifications.stream()
                .map(notificationVo -> new NotificationDto(
                        notificationVo.getNoticeNo(),
                        notificationVo.getContents(),
                        new CommonCodeAndNameDto(notificationVo.getCategoryCode(), notificationVo.getCategoryCodeName()),
                        notificationVo.getReadYn(),
                        notificationVo.getCreateDateTime()
                ))
                .toList();

    }

    @Transactional
    public boolean readNotifications(String userNo, NotificationReadDto notificationReadDto) {
        if (notificationMapper.updateReadYn(userNo, notificationReadDto.getNotificationIdList())
                != notificationReadDto.getNotificationIdList().size()) {
            throw new RuntimeException("알림 읽음 처리에 실패한 항목이 있습니다.");
        }

        return true;
    }
}
