package com.be.sportizebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
public class SportizeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportizeBeApplication.class, args);
    }

}
