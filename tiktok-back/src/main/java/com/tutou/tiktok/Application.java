package com.tutou.tiktok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sobinbin
 * @school BUPT
 * @create 2020-05-30 13:36
 */
@SpringBootApplication(scanBasePackages = {"com.tutou.tiktok"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
