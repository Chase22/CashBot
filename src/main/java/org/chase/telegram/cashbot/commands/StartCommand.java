package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class StartCommand extends CashCommand {
	private static final String IDENTIFIER = "start";
	private static final String DESCRIPTION = "";
	private static final String EXTENDED_DESCRIPTION = "";

    private final StartCommandGroup startCommandGroup;
    private final StartCommandUser startCommandUser;

    public StartCommand(final StartCommandGroup startCommandGroup, final StartCommandUser startCommandUser) {
		super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.startCommandGroup = requireNonNull(startCommandGroup, "startCommandGroup");
        this.startCommandUser = requireNonNull(startCommandUser, "startCommandUser");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {
        Chat chat = message.getChat();

        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            startCommandGroup.verify(message, arguments, absSender);
        } else if (chat.isUserChat()) {
            startCommandUser.verify(message, arguments, absSender);
        } else {
            throw new VerificationException("This bot can only be used in Private or Group chats");
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException{
        final Chat chat = message.getChat();

        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            return startCommandGroup.executeCommand(absSender, message, arguments);
        } else if (chat.isUserChat()) {
            return startCommandUser.executeCommand(absSender, message, arguments);
        }
        return Optional.empty();
    }

    @Override
	public String getExtendedDescription() {
		return EXTENDED_DESCRIPTION;
	}
}
