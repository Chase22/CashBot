package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.CashChat.CashBotReply;
import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.bot.GroupUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import sun.misc.resources.Messages;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class StartCommand extends CashCommand {
	private static final String IDENTIFIER = "start";
	private static final String DESCRIPTION = "";
	private static final String EXTENDED_DESCRIPTION = "";

	private final CashChatService cashChatService;
    private final CashUserService cashUserService;

    public StartCommand(CashChatService cashChatService, CashUserService cashUserService) {
		super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    protected Optional<CashBotReply> executeCommand(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException{
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            Optional<CashChat> optionalCashChat = cashChatService.getChatChatById(chat.getId().toString());

            if (optionalCashChat.isPresent()) {
                return Optional.of(new CashBotReply(optionalCashChat.get().getChatId(), "Bot already running"));
            } else {
                if (GroupUtils.isAdministrator(absSender, chat, user)) {
                    CashChat cashChat = cashChatService.createDefault(chat.getId().toString());
                    return Optional.of(new CashBotReply("Bot started %s", cashChat.getChatId(), cashChat));
                } else {
                    return Optional.of(new CashBotReply("This command is only to be used by Administrators", chat.getId().toString()));
                }
            }
        } else if (chat.isUserChat()) {
            Optional<CashUser> cashUserOptional = cashUserService.getById(user.getId());
            if (cashUserOptional.isPresent()) {
                return Optional.of(new CashBotReply("Bot already running", chat.getId().toString()));
            } else if (cashUserOptional.get().getChatId().isEmpty()) {
                return Optional.of(new CashBotReply("Chat registered", chat.getId().toString()));
            } else {
                return Optional.of(new CashBotReply("User registered", chat.getId().toString()));
            }
        }
        return Optional.empty();
    }

    @Override
	public String getExtendedDescription() {
		return EXTENDED_DESCRIPTION;
	}
}
