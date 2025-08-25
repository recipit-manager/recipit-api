package toy.recipit.common;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
class MessageProvider {
    private static MessageSource messageSource;

    MessageProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    static String getMessage(String key) {

        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
