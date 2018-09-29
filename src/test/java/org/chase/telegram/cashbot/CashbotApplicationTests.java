package org.chase.telegram.cashbot;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({
        "BOT_TOKEN=340581395:AAE1hZDVsts3My4yXcjA4PprGmGFLiAq9l0",
        "PORT=8080"})
public class CashbotApplicationTests {

    @BeforeClass
    public static void initializeApi() {
        //ApiContextInitializer.init();
    }

    @Test
    public void contextLoads() {
    }

}
