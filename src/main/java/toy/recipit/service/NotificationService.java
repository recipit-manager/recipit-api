package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.response.CommonCodeAndNameDto;
import toy.recipit.controller.dto.response.NotificationDto;
import toy.recipit.mapper.NotificationMapper;
import toy.recipit.mapper.vo.NotificationVo;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final CommonService commonService;
    private final NotificationMapper notificationMapper;

    public List<NotificationDto> getNotifications(String userNo) {
        List<NotificationVo> notifications = notificationMapper.getNotifications(userNo);

        List<String> typeCodes = notifications.stream()
                .map(NotificationVo::getCategoryCode)
                .distinct()
                .toList();

        Map<String, String> codeNameMap = commonService.getCommonCodeNameMap(Constants.GroupCode.NOTIFICATION, typeCodes);


        return notifications.stream()
                .map(notificationVo ->
                        new NotificationDto(
                                notificationVo.getNoticeNo(),
                                notificationVo.getContents(),
                                new CommonCodeAndNameDto(
                                        notificationVo.getCategoryCode(),
                                        codeNameMap.get(notificationVo.getCategoryCode())
                                ),
                                notificationVo.getReadYn(),
                                notificationVo.getCreateDateTime()
                        )
                )
                .toList();

    }
}
