package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.VerifierService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class StartCommand extends CashCommand {
	private static final String IDENTIFIER = "start";
	private static final String DESCRIPTION = "";
	private static final String EXTENDED_DESCRIPTION = "";

    private final StartCommandGroup startCommandGroup;
    private final StartCommandUser startCommandUser;

    public StartCommand(final VerifierService verifierService, final StartCommandGroup startCommandGroup, final StartCommandUser startCommandUser) {
		super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, verifierService);
        this.startCommandGroup = requireNonNull(startCommandGroup, "startCommandGroup");
        this.startCommandUser = requireNonNull(startCommandUser, "startCommandUser");
    }

    @Override
    protected void verify(final User user, final Chat chat, final String[] arguments, final VerifierService verifierService, final AbsSender absSender) throws VerificationException {
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            startCommandGroup.verify(user, chat, arguments, verifierService, absSender);
        } else if (chat.isUserChat()) {
            startCommandUser.verify(user, chat, arguments, verifierService, absSender);
        }
        throw new VerificationException("This bot can only be used in Private or Group chats");
    }

    @Override
    protected Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException{
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            return startCommandGroup.executeCommand(absSender, user, chat, arguments);
        } else if (chat.isUserChat()) {
            return startCommandUser.executeCommand(absSender, user, chat, arguments);
        }
        return Optional.empty();
    }

    @Override
	public String getExtendedDescription() {
		return EXTENDED_DESCRIPTION;
	}
}
