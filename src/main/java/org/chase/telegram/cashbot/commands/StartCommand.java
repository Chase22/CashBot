package org.chase.telegram.cashbot.commands;

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
            Optional<CashChat> optionalCashChat = cashChatService.getChatChatById(chat.getId());

            if (optionalCashChat.isPresent()) {
                return Optional.of(new CashBotReply(optionalCashChat.get().getChatId(), "Bot already running"));
            } else {
                if (GroupUtils.isAdministrator(absSender, chat, user)) {
                    CashChat cashChat = cashChatService.createDefault(chat.getId());
                    return Optional.of(new CashBotReply(cashChat.getChatId(), "Bot started %s", cashChat));
                } else {
                    return Optional.of(new CashBotReply(chat.getId(), "This command is only to be used by Administrators"));
                }
            }
        } else if (chat.isUserChat()) {
            Optional<CashUser> cashUserOptional = cashUserService.getById(user.getId());
            if (cashUserOptional.isPresent()) {
                CashUser cashUser = cashUserOptional.get();
                if (cashUser.getChatId() == 0) {
                    cashUser.setChatId(chat.getId());
                    cashUserService.save(cashUser);
                    return Optional.of(new CashBotReply(chat.getId(), "Chat registered"));
                } else {
                    return Optional.of(new CashBotReply(chat.getId(), "Bot already running"));
                }
            } else {
                cashUserService.save(new CashUser(user.getId(), chat.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
                return Optional.of(new CashBotReply(chat.getId(), "User registered"));
            }
        }
        return Optional.empty();
    }

    @Override
	public String getExtendedDescription() {
		return EXTENDED_DESCRIPTION;
	}
}
