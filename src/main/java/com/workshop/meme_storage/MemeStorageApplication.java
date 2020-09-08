package com.workshop.meme_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemeStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemeStorageApplication.class, args);
    }

}
