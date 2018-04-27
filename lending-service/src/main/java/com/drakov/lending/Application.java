package com.drakov.lending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {
        CodecsAutoConfiguration.class,
        JmxAutoConfiguration.class
})
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
