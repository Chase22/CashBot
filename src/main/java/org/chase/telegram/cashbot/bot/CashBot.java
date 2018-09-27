package org.chase.telegram.cashbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.logging.Logger;

@Component
public class CashBot extends TelegramLongPollingCommandBot {
    public static Logger LOGGER = Logger.getLogger(CashBot.class.getName());

    @Value("telegram.bot.name")
    public static String BOT_USERNAME;

    @Value("telegram.bot.token")
    public static String BOT_TOKEN;

    public CashBot() {
        super(new DefaultBotOptions(), true, BOT_USERNAME);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            LOGGER.info(update.getMessage().getText());
        }
    }

    @Override
    public String getBotToken() {
        return null;
    }

    private void registerCommands() {
        registerAll(
                new HelpCommand()
        );
    }
}
