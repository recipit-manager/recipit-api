package toy.recipit.controller.dto.response.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import toy.recipit.controller.dto.response.ApiResponse;

@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

    private final MessageSource messageSource;

    private String resolveMessage(ApiResponse.Result result, Object... args) {
        return messageSource.getMessage(
                result.getMessageKey(),
                args,
                LocaleContextHolder.getLocale()
        );
    }

    public <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ApiResponse.Result.SUCCESS.getCode(),
                resolveMessage(ApiResponse.Result.SUCCESS),
                data
        );
    }

    public <T> ApiResponse<T> error(ApiResponse.Result result, Object... args) {
        return new ApiResponse<>(
                result.getCode(),
                resolveMessage(result, args),
                null
        );
    }
}

