package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.account.AccountService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.session.Session;
import org.chase.telegram.cashbot.session.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import static java.util.Objects.requireNonNull;
import static liquibase.util.StringUtils.isNotEmpty;

@Component
@Slf4j
public class CashBot extends TelegramLongPollingCommandBot {

    private final String botToken;
    private final AccountService accountService;
    private final SessionService sessionService;
    private final CashUserService cashUserService;

    public CashBot(@Value("${telegram.bot.name}") final String bot_username,
                   @Value("${BOT_TOKEN}") final String bot_token,
                   final CommandRegisterService commandRegisterService,
                   final AccountService accountService, final SessionService sessionService, final CashUserService cashUserService) {
        super(bot_username);
        botToken = requireNonNull(bot_token, "bot_token");
        this.accountService = accountService;
        this.sessionService = sessionService;
        this.cashUserService = cashUserService;

        commandRegisterService.registerCommands(this);
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
            Message message = update.getMessage();
            Session session = sessionService.getSession(message);
            String activeCommand = session.getActiveCommand();

            if (isNotEmpty(activeCommand)) {
                executeCommand(message, session, true);
            } else {
                accountService.handleMessage(message);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            Session session = sessionService.getSession(query.getMessage().getChatId(), query.getFrom().getId());
            session.setCallbackQueryId(query.getId());
            session.setCallbackQueryData(query.getData());
            session.setCallbackQueryMessageId(query.getMessage().getMessageId());
            session.setCallbackQueryChatId(query.getMessage().getChatId());
            sessionService.save(session);

            String activeCommand = session.getActiveCommand();
            if (isNotEmpty(activeCommand)) {
                executeCommand(query.getMessage(), session, false);
            }
        }
    }

    private void executeCommand(final Message message, final Session session, final boolean replyTo) {
        try {
            ((CashCommand) getRegisteredCommand(session.getActiveCommand()))
                    .executeCommand(this, message, null, session)
                    .ifPresent(cashBotReply -> {
                        try {
                            if (replyTo) {
                                cashBotReply.sendMessage(this, message.getMessageId());
                            } else {
                                cashBotReply.sendMessage(this);
                            }
                        } catch (TelegramApiRequestException e) {
                            log.error("Couldn't send Error to chat: " + e.getApiResponse(), e);
                        } catch (TelegramApiException e) {
                            log.error("Couldn't send Error to chat", e);
                        }
                    });
        } catch (TelegramApiException e) {
            log.error("Error executing command", e);
        }
        session.setActiveCommand(null);
        sessionService.save(session);
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


}
