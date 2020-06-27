package com.yurets_y.payment_statistic_web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class MessageProvider {

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println(locale);
        accessor = new MessageSourceAccessor(messageSource, locale);
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String get(String code, @Nullable Object[] params, Locale locale){
        return messageSource.getMessage(code,params,locale);
    }

}
