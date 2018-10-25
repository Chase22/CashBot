package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
@DisableCommand
public class StartCommandUser extends CashCommand {

    private final CashUserService cashUserService;

    public StartCommandUser(CashUserService cashUserService) {
		super("startCommandUser", "", "");
        this.cashUserService = requireNonNull(cashUserService, "cashUserService");
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException{
        final Chat chat = message.getChat();
        final User user = message.getFrom();

        if (cashUserService.getById(message.getFrom().getId()).isPresent()) {
            return Optional.of(new CashBotReply(chat.getId(), "Bot is already running"));
        }
        cashUserService.save(new CashUser(user.getId(), chat.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
        return Optional.of(new CashBotReply(chat.getId(), "User registered"));
    }

    @Override
	public String getExtendedDescription() {
		return null;
	}
}
