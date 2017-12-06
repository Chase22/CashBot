package org.chase.telegram.cashbot.commands.account;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.filter.CashChatCommandFilter;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class OpenAccountCommand extends CheckableBotCommand {
	
	private static final String IDENTIFIER = "openAccount"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("OpenAccountCommand.0"); //$NON-NLS-1$

	public OpenAccountCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new CashChatCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {		
		EntityManager manager = EntityUtility.getEntityManager();
		
		SendMessage reply = new SendMessage(chat.getId(), ""); //$NON-NLS-1$
		if (Account.exists(manager, user.getId(), chat.getId())) {
			reply.setText(Messages.getString("OpenAccountCommand.1")); //$NON-NLS-1$
		} else {
			if (!CashUser.exists(manager, user.getId())) {
				CashUser cashUser = CashUser.create(EntityUtility.getEntityManager(), user.getId(), user.getUserName(), user.getFirstName(), user.getLastName());
			}
			Account account = Account.create(manager, user.getId(), chat.getId());
			CashChat cashGroup = CashChat.load(manager, account.getGroupID());
			account.setBalance(manager,CashChat.load(manager, chat.getId()).getStartAmount());
			manager.close();
			reply.setText(Messages.getFormatString("OpenAccountCommand.2", account.getBalance(), cashGroup.getCurrencyName())); //$NON-NLS-1$
		}
		if (!reply.getText().isEmpty()) {
			try {
				absSender.execute(reply);
			} catch (TelegramApiException e) {
				BotLogger.error(this.getClass().getSimpleName(), e);
			}
		}
	}

}
