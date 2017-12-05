package org.chase.telegram.cashbot.commands.base.filter;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.BotCommandFilter;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class CashUserCommandFilter extends BotCommandFilter {

	@Override
	public boolean filter(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage) {
		try {

				if (!CashUser.exists(EntityUtility.getEntityManager(), user.getId())) {
					SendMessage reply = new SendMessage(chat.getId(), Messages.getString("CashUserCommandFilter.0")); //$NON-NLS-1$
					if (!supressMessage) absSender.execute(reply);
					return false;
				} else {
					return true;
				}	
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
			return false;
		}
	}

}
