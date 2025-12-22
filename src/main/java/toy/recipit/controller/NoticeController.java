package toy.recipit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.request.NoticeItemDto;
import toy.recipit.controller.dto.request.NoticeRequestDto;
import toy.recipit.websocket.WebSocketHandler;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@Tag(name = "알림", description = "Recipit 서비스 구성에서 알림에 관련된 API 정보를 제공합니다.")
public class NoticeController {
    private final WebSocketHandler noticeWebSocketHandler;

    @Value("${internal.auth-key}")
    private String internalAuthKey;

    @PostMapping("/dispatch")
    @Operation(summary = "새로운 알림 신호 수신", description = "새로운 알림이 생성된 경우 신호를 수신하여 메세지를 반환합니다..")
    public ResponseEntity<Void> noticeDispatch(
            @RequestBody NoticeRequestDto request,
            @RequestHeader("AuthenticationKey") String authenticationKey
    ) {
        if (!internalAuthKey.equals(authenticationKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (NoticeItemDto item : request.getNoticeItems()) {
            noticeWebSocketHandler.sendMessage(item.getUserNo(), Constants.Notice.NEW_NOTICE_MSG);
        }

        return ResponseEntity.ok().build();
    }
}
