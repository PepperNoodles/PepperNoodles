package com.peppernoodles;

import com.peppernoodles.common.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class PepperNoodlesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PepperNoodlesApplication.class, args);
    }
}
