package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
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
    public boolean readNotifications(String userNo, List<String> notificationIdList) {
        if (notificationMapper.updateReadYn(userNo, notificationIdList, Constants.Yn.YES)
                != notificationIdList.size()) {
            throw new IllegalArgumentException("notification.notValidNoticeNoList");
        }

        return true;
    }

    public boolean isUnreadNotificationExists(String userNo) {
        return notificationMapper.isUnreadNotificationExists(userNo, Constants.Yn.NO);
    }
}
