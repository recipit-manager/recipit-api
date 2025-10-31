package toy.recipit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.request.NoticeItemDto;
import toy.recipit.controller.dto.request.NoticeRequestDto;
import toy.recipit.websocket.WebSocketHandler;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final WebSocketHandler noticeWebSocketHandler;

    @PostMapping("/dispatch")
    public ResponseEntity<Void> noticeDispatch(@RequestBody NoticeRequestDto request) {
        System.out.println("📩 알림 요청 수신: " + request.getNoticeItems().size() + "건");

        for (NoticeItemDto item : request.getNoticeItems()) {
            noticeWebSocketHandler.sendMessage(item.getUserNo(), Constants.Notice.NEW_NOTICE_MSG);
        }

        return ResponseEntity.ok().build();
    }
}
