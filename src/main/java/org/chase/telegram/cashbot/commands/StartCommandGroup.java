package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.VerifierService;
import org.chase.telegram.cashbot.bot.CashBot;
import org.chase.telegram.cashbot.bot.GroupUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class StartCommandGroup extends CashCommand {

	private final CashChatService cashChatService;
    private final CashBot cashBot;

    public StartCommandGroup(CashChatService cashChatService, final VerifierService verifierService, final CashBot cashBot) {
		super(null, null, null, verifierService);
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.cashBot = requireNonNull(cashBot, "cashBot");
    }

    @Override
    protected void verify(final User user, final Chat chat, final String[] arguments, final VerifierService verifierService) throws VerificationException {
        cashChatService.getById(chat.getId()).ifPresent((cashChat) -> new VerificationException("Bot is already running"));
        try {
            if (!GroupUtils.isAdministrator(cashBot,chat, user)) {
                throw new VerificationException("This command can only be executed by Admins");
            }
        } catch (TelegramApiException e) {
            log.error("Error checking isAdmin for User {} in chat {}", user.getUserName(), chat.getTitle());
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException{
        CashChat cashChat = cashChatService.createDefault(chat.getId(), chat.getTitle());
        return Optional.of(new CashBotReply(cashChat.getChatId(), "Bot started %s", cashChat));
    }




    @Override
	public String getExtendedDescription() {
		return null;
	}
}
