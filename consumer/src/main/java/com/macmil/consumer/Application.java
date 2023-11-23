package com.macmil.consumer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {

        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public CommandLineRunner callProviderApi(ProviderApiClient client) {

        return args -> log.info(client.fetch("widget").toString());
    }
}
