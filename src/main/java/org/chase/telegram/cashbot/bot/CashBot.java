package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.session.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
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
    private final SessionService sessionService;

    public CashBot(@Value("${telegram.bot.name}") final String bot_username,
                   @Value("${BOT_TOKEN}") final String bot_token,
                   final CommandRegisterService commandRegisterService,
                   final AccountService accountService, final SessionService sessionService) {
        super(bot_username);
        botToken = requireNonNull(bot_token, "bot_token");
        this.accountService = accountService;
        this.sessionService = sessionService;

        commandRegisterService.registerCommands(this, new HelpCommand());
    }

    @Override
    protected void processInvalidCommandUpdate(final Update update) {
        try {
            execute(
                    new SendMessage(update.getMessage().getChatId(), "Command Unknown")
                            .setReplyToMessageId(update.getMessage().getMessageId())
            );
        } catch (TelegramApiException e) {
            log.error("Error sending message for unknown command", e);
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Optional<Session> session = sessionService.getSession(update.getMessage());
            if (session.isPresent()) {
                String activeCommand = (String) session.get().getAttribute("activeCommand");
                if (activeCommand != null) {
                    try {
                        ((CashCommand) getRegisteredCommand(activeCommand))
                                .executeCommand(this, update.getMessage(), null, session.get())
                                .ifPresent(cashBotReply -> {
                                    try {
                                        cashBotReply.sendMessage(this, update.getMessage().getMessageId());
                                    } catch (TelegramApiException e) {
                                        log.error("Couldn't send Error to chat", e);
                                    }
                                });
                    } catch (TelegramApiException e) {
                        log.error("Error executing command", e);
                    }

                    log.debug("recieved update: {}", update);
                    if (update.hasMessage()) {
                        Message message = update.getMessage();

                        log.debug("found message: {}", message);

                        accountService.handleMessage(message);
                    }
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }



}
