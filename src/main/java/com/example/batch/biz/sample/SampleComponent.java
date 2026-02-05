package com.example.batch.biz.sample;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SampleComponent {

    private final Environment env;


    public SampleComponent(Environment env) {
        this.env = env;

        String projectName = env.getProperty("project.name");

        log.debug("projectName {}", projectName);
    }
}
