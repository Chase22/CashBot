package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.bot.TelegramUserRightService;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.anotations.AdminCommand;
import org.chase.telegram.cashbot.session.Session;
import org.chase.telegram.cashbot.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singleton;

@Slf4j
public abstract class CashCommand extends ManCommand {

    @Autowired
    private TelegramUserRightService telegramUserRightService;

    @Autowired
    private  CashUserService cashUserService;

    @Autowired
    private SessionService sessionService;

    public CashCommand(String commandIdentifier, String description, String extendedDescription) {
        super(commandIdentifier, description, extendedDescription);
    }

    @Override
    public void processMessage(final AbsSender absSender, final Message message, final String[] arguments) {
        Session session = sessionService.getSession(message);
        cashUserService.getAndUpdateUser(message.getFrom(), message.getChatId());

        try {
            if (this.getClass().isAnnotationPresent(AdminCommand.class)) {
                if (!telegramUserRightService.isAdministrator(absSender, message.getChat(), message.getFrom())) {
                    absSender.execute(
                            new SendMessage()
                            .setReplyToMessageId(message.getMessageId())
                            .setChatId(message.getChatId())
                            .setText("This command can only be used by admins"));
                }
            }
            executeCommand(absSender, message, arguments, session).ifPresent(cashBotReply -> {
                try {
                    cashBotReply.sendMessage(absSender, message.getMessageId());
                } catch (TelegramApiException e) {
                    log.error("Couldn't send Error to chat", e);
                }
            });
        } catch (TelegramApiException e) {
            log.error("Error executing command", e);
        }
    }

    public Optional<CashBotReply> executeCommand(AbsSender absSender, Message message, String[] arguments, Session session) throws TelegramApiException {
        return executeCommand(absSender, message, arguments);
    }

    public abstract Optional<CashBotReply> executeCommand(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException;

    public Set<HelpCategory> getCategory() {
        return singleton(HelpCategory.Misc);
    }

    @Override
    public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) {}

}
