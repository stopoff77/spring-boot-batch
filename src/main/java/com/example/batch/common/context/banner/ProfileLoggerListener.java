package com.example.batch.common.context.banner;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpMethod;

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
        String webClientType = event.getEnvironment().getProperty("web.web-client.type");

        System.out.println("====================================");
        System.out.println("   WebClient TYPE");
        System.out.println("====================================");
        System.out.println("          " + webClientType);
        System.out.println("====================================");
//        log.info("====================================");
//        log.info("   WebClient TYPE");
//        log.info("====================================");
//        log.info("          {}", webClientType);
//        log.info("====================================");


        // 아래 클래스 정보 가져오는 소스
        Class<?> clazz = HttpMethod.class;

        System.out.println("===== " + clazz.getSimpleName() + " 클래스 정보 =====");
        System.out.println("FQCN         : " + clazz.getName());
        System.out.println("isEnum       : " + clazz.isEnum());
        System.out.println("ClassLoader  : " + clazz.getClassLoader());
        System.out.println("Location     : " +
                clazz.getProtectionDomain().getCodeSource().getLocation());
        System.out.println("=================================");
//        log.info("===== {} 클래스 정보 =====", clazz.getSimpleName());
//        log.info("FQCN         : {}", clazz.getName());
//        log.info("isEnum       : {}", clazz.isEnum());
//        log.info("ClassLoader  : {}", clazz.getClassLoader());
//        log.info("Location     : {}", clazz.getProtectionDomain().getCodeSource().getLocation());
//        log.info("=================================");

    }
}
