package com.zoomers.GameSetMatch.scheduler;

import com.zoomers.GameSetMatch.services.AvailabilityService;
import com.zoomers.GameSetMatch.services.RegistrantService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SpringConfig implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        SpringConfig.context = context;
    }
}
