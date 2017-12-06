package org.chase.telegram.cashbot.commands.account;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.ReplyToCommand;
import org.chase.telegram.cashbot.commands.base.filter.AdminCommandFilter;
import org.chase.telegram.cashbot.commands.base.filter.CashChatCommandFilter;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class SetAccountToCommand extends ReplyToCommand {
	private static final String IDENTIFIER = "setaccountto"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("SetAccountToCommand.1"); //$NON-NLS-1$

	public SetAccountToCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new AdminCommandFilter());
		addFilter(new CashChatCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments,
			Integer repliedToID) {
		try {
			int amount = Integer.parseInt(arguments[0]);
			EntityManager manager = EntityUtility.getEntityManager();
			Account account = Account.load(manager, repliedToID, chat.getId());
			CashUser cashUser = CashUser.load(manager, repliedToID);
			CashChat cashGroup = CashChat.load(manager, account.getGroupID());
			account.setBalance(manager, amount);
			reply.setText(Messages.getFormatString("AddToAccountCommand.2", cashUser.getName(), account.getBalance(), cashGroup.getCurrencyName()));
		} catch (NumberFormatException e) {
			reply.setText(Messages.getFormatString("IntegerException.1", Integer.MIN_VALUE, Integer.MAX_VALUE));
		}
	}

}
