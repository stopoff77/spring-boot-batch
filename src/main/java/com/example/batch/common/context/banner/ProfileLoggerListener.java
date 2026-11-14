package com.example.batch.common.context.banner;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 배너 출력 전에 Active Profile을 줄바꿈으로 보기 좋게 출력
 */
@Slf4j
public class ProfileLoggerListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String[] activeProfiles = event.getEnvironment().getActiveProfiles();
        String   profilesDisplay;

        if (activeProfiles.length == 0) {
            profilesDisplay = "default";
        } else {
            // 줄바꿈으로 구분
//            profilesDisplay = Arrays.stream(activeProfiles)
//                    .collect(Collectors.joining(System.lineSeparator()));
            profilesDisplay = Arrays.stream(activeProfiles)
                    .map(profile -> profile)
//                    .collect(Collectors.joining(System.lineSeparator()));
                    .collect(Collectors.joining(", "));
        }

        System.out.println("====================================");
        System.out.println("   ACTIVE PROFILES BEFORE BANNER    ");
        System.out.println("====================================");
        System.out.println("          " + profilesDisplay);
        System.out.println("====================================");
//        log.info("====================================");
//        log.info("   ACTIVE PROFILES BEFORE BANNER    ");
//        log.info("====================================");
//        log.info("          {}", profilesDisplay);
//        log.info("====================================");


        //
        String webApplicationType = event.getEnvironment().getProperty("spring.main.web-application-type");

        System.out.println("====================================");
        System.out.println("   WEB-APPLICATION-TYPE");
        System.out.println("====================================");
        System.out.println("          " + webApplicationType);
        System.out.println("====================================");
//        log.info("====================================");
//        log.info("   WebClient TYPE");
//        log.info("====================================");
//        log.info("          {}", webClientType);
//        log.info("====================================");
    }
}
