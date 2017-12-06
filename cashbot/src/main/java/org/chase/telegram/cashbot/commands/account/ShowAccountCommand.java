package org.chase.telegram.cashbot.commands.account;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.filter.AccountCommandFilter;
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

public class ShowAccountCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "showBalance"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("ShowAccountCommand.1"); //$NON-NLS-1$

	public ShowAccountCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new AccountCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		EntityManager manager = EntityUtility.getEntityManager();
		Account account = Account.load(manager, user.getId(), chat.getId());
		CashChat cashChat = CashChat.load(manager, chat.getId());
		CashUser cashUser = CashUser.load(manager, user.getId());
		
		SendMessage reply = new SendMessage();
		reply.setChatId(chat.getId());
		reply.setText(Messages.getFormatString("ShowAccountCommand.2", cashUser.getName(), account.getBalance(), cashChat.getCurrencyName())); //$NON-NLS-1$
		
		try {
			absSender.execute(reply);
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}
}
