package ru.practicum.shareit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointsLogger implements ApplicationRunner {

    private final ApplicationContext context;

    public EndpointsLogger(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            RequestMappingHandlerMapping mapping =
                    (RequestMappingHandlerMapping) context.getBean("requestMappingHandlerMapping");

            System.out.println("==== Registered Endpoints ====");
            mapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
                System.out.printf("%s -> %s%n", requestMappingInfo, handlerMethod);
            });
            System.out.println("==============================");

        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch endpoints: " + e.getMessage());
        }
    }
}
