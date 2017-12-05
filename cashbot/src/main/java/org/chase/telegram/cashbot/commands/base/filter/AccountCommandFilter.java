package org.chase.telegram.cashbot.commands.base.filter;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.account.OpenAccountCommand;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class AccountCommandFilter extends CashUserCommandFilter {
	@Override
	public boolean filter(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage) {
		try {
			if (new CashChatCommandFilter().filter(absSender, user, chat, arguments, supressMessage)) {
				if (super.filter(absSender, user, chat, arguments, supressMessage)) {
					if (!Account.exists(EntityUtility.getEntityManager(), user.getId(), chat.getId())) {
						SendMessage reply = new SendMessage(chat.getId(), Messages.getFormatString(
								"AccountCommandFilter.0", new OpenAccountCommand().getCommandIdentifier())); //$NON-NLS-1$
						if (!supressMessage)
							absSender.execute(reply);
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
			return false;
		}
	}

}
