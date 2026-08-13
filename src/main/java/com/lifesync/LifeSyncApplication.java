package com.lifesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // needed later for the Reminder/Notification scheduler (Stage 9)
public class LifeSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeSyncApplication.class, args);
    }
}
