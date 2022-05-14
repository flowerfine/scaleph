package cn.sliew.breeze.api.util;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author gleiyu
 */
@Component
public class I18nUtil {
    private static MessageSource messageSource;

    public I18nUtil(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

    public static String get(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return msgKey;
        }
    }

    public static String get(String msgKey, String defaultMsg) {
        try {
            return messageSource.getMessage(msgKey, null, defaultMsg, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return defaultMsg;
        }
    }
}
