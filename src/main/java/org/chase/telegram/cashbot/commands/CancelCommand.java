package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.chase.telegram.cashbot.session.Session;
import org.chase.telegram.cashbot.session.SessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@EnableCommand
public class CancelCommand extends CashCommand {
    private static final String IDENTIFIER = "cancel";
    private static final String DESCRIPTION = "Cancels the running command";
    private static final String EXTENDED_DESCRIPTION = DESCRIPTION;

    private final SessionService sessionService;

    public CancelCommand(final SessionService sessionService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.sessionService = sessionService;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException {
        return Optional.empty();
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments, final Session session) throws TelegramApiException {
        if (isNotBlank(session.getActiveCommand())) {
            String activeCommand = session.getActiveCommand();
            sessionService.delete(session);
            return Optional.of(new CashBotReply(message.getChatId(), "Command %s canceled", activeCommand));
        } else {
            return Optional.of(new CashBotReply(message.getChatId(), "No command active"));
        }
    }
}
