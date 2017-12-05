package org.chase.telegram.cashbot.commands.base.filter;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.BotCommandFilter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class GroupCommandFilter extends BotCommandFilter {

	@Override
	public boolean filter(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage) {
		if (!chat.isGroupChat() && !chat.isSuperGroupChat()) {
			SendMessage reply = new SendMessage(chat.getId(), Messages.getString("GroupCommandFilter.0")); //$NON-NLS-1$
			try {
				if (!supressMessage) absSender.execute(reply);
				return false;
			} catch (TelegramApiException e) {
				BotLogger.error(this.getClass().getSimpleName(), e);
				return false;
			}
		} else {
			return true;
		}
	}

}
