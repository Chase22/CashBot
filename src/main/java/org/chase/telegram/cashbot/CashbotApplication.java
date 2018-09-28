package org.chase.telegram.cashbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class CashbotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        
        SpringApplication.run(CashbotApplication.class, args
        );
    }
}
