package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.bot.GroupUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class StartCommandGroup extends CashCommand {

	private final CashChatService cashChatService;

    public StartCommandGroup(CashChatService cashChatService) {
		super("startCommandGroup", "", "");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {
        cashChatService.getById(message.getChat().getId()).ifPresent((cashChat) -> new VerificationException("Bot is already running"));
        try {
            if (!GroupUtils.isAdministrator(absSender , message.getChat(), message.getFrom())) {
                throw new VerificationException("This command can only be executed by Admins");
            }
        } catch (TelegramApiException e) {
            log.error("Error checking isAdmin for User {} in chat {}", message.getFrom().getUserName(), message.getChat().getTitle());
        }
    }

    @Override
    protected Optional<CashBotReply> executeCommand(AbsSender absSender, Message message, String[] arguments) throws TelegramApiException{
        final Chat chat = message.getChat();

        CashChat cashChat = cashChatService.createDefault(chat.getId(), chat.getTitle());
        return Optional.of(new CashBotReply(cashChat.getChatId(), "Bot started %s", cashChat));
    }




    @Override
	public String getExtendedDescription() {
		return null;
	}
}
