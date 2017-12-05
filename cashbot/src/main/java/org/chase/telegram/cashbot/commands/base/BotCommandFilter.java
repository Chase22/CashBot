package org.chase.telegram.cashbot.commands.base;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public abstract class BotCommandFilter {
	public abstract boolean filter(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage);
}
