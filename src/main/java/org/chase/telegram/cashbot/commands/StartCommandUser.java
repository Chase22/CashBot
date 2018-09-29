package org.chase.telegram.cashbot.commands;

import com.google.common.base.VerifyException;
import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.VerifierService;
import org.chase.telegram.cashbot.bot.GroupUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class StartCommandUser extends CashCommand {

    private final CashUserService cashUserService;

    public StartCommandUser(CashChatService cashChatService, CashUserService cashUserService, final VerifierService verifierService) {
		super(null, null, null, verifierService);
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    protected void verify(final User user, final Chat chat, final String[] arguments, final VerifierService verifierService) throws VerificationException {
        cashUserService.getById(user.getId()).ifPresent((cashUser) -> new VerificationException("Bot is already running"));
    }

    @Override
    protected Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException{
        cashUserService.save(new CashUser(user.getId(), chat.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
        return Optional.of(new CashBotReply(chat.getId(), "User registered"));
    }

    @Override
	public String getExtendedDescription() {
		return null;
	}
}
