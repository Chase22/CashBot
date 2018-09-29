package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.Account.AccountService;
import org.chase.telegram.cashbot.Account.commands.CloseAccount;
import org.chase.telegram.cashbot.flags.Flag;
import org.chase.telegram.cashbot.flags.FlagService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class CashBot extends TelegramLongPollingCommandBot {

    private final String botToken;
    private final AccountService accountService;

    public CashBot(@Value("${telegram.bot.name}") final String bot_username,
                   @Value("${BOT_TOKEN}") final String bot_token,
                   final CommandRegisterService commandRegisterService,
                   final AccountService accountService) {
        super(bot_username);
        botToken = requireNonNull(bot_token, "bot_token");
        this.accountService = accountService;

        commandRegisterService.registerCommands(this, new HelpCommand());
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        log.debug("recieved update: {}", update);
        if (update.hasMessage()) {
            Message message = update.getMessage();

            log.debug("found message: {}", message);

            accountService.handleMessage(message);
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
