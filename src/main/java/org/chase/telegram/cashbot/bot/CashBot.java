package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CashBot extends TelegramLongPollingCommandBot {

    private final String botToken;

    public CashBot(@Value("${telegram.bot.name}") final String bot_username, @Value("${telegram.bot.token}") String bot_token) {
        super(bot_username);
        botToken = bot_token;
        registerCommands();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            log.info(update.getMessage().getText());
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void registerCommands() {
        registerAll(
                new HelpCommand()
        );
    }
}
