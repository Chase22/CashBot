package org.chase.telegram.cashbot.commands.start;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.commands.anotations.AdminCommand;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.anotations.DisableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
@AdminCommand
@DisableCommand
public class StartCommandGroup extends CashCommand {

	private final CashChatService cashChatService;

    public StartCommandGroup(final CashChatService cashChatService) {
		super("startCommandGroup", "", "");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    @Override
    public Optional<CashBotReply> executeCommand(AbsSender absSender, Message message, String[] arguments) {
        final Chat chat = message.getChat();

        if (cashChatService.getById(message.getChatId()).isPresent()) {
            return Optional.of(new CashBotReply(message.getChatId(), "Bot is already running"));
        }
        CashChat cashChat = cashChatService.createDefault(chat.getId(), chat.getTitle());
        return Optional.of(new CashBotReply(cashChat.getChatId(), "Bot started %s", cashChat.toChatString()));
    }

    @Override
	public String getExtendedDescription() {
		return null;
	}
}
