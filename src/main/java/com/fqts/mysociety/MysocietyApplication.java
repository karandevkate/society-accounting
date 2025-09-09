package com.fqts.mysociety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MysocietyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysocietyApplication.class, args);
    }

}
