package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class CashBot extends TelegramLongPollingCommandBot {

    private final String botToken;
    private final CommandRegisterService commandRegisterService;

    public CashBot(@Value("${telegram.bot.name}") final String bot_username, @Value("${BOT_TOKEN}") final String bot_token, final CommandRegisterService commandRegisterService) {
        super(bot_username);
        botToken = requireNonNull(bot_token, "bot_token");

        this.commandRegisterService = requireNonNull(commandRegisterService, "commandRegisterService");
        commandRegisterService.registerCommands(this, new HelpCommand());
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

}
