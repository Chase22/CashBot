package org.chase.telegram.cashbot.commands.account;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.ReplyToCommand;
import org.chase.telegram.cashbot.commands.base.filter.CashChatCommandFilter;
import org.chase.telegram.cashbot.commands.base.filter.CashUserCommandFilter;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class TransferToAccountCommand extends ReplyToCommand {
	private static final String IDENTIFIER = "transfer"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("TransferToAccountCommand.1"); //$NON-NLS-1$

	public TransferToAccountCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new CashChatCommandFilter());
		addFilter(new CashUserCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments,
			Integer repliedToID) {

		try {
			int amount = Integer.parseInt(arguments[0]);
			if (amount < 0) {
				reply.setText(Messages.getString("TransferToAccountCommand.3"));
			} else {
				EntityManager manager = EntityUtility.getEntityManager();
				Account source = Account.load(manager, user.getId(), chat.getId());
				Account target = Account.load(manager, repliedToID, chat.getId());
				if (source.getBalance() - amount < 0) {
					reply.setText(Messages.getString("TransferToAccountCommand.2"));
				} else {
					source.modifyBalance(manager, amount * (-1));
					target.modifyBalance(manager, amount);
					reply.setText(Messages.getFormatString("TransferToAccountCommand.4", amount,
							CashChat.load(manager, chat.getId()).getCurrencyName(), source.getUser().getName(),
							target.getUser().getName()));
				}
			}
		} catch (NumberFormatException e) {
			reply.setText(Messages.getFormatString("IntegerException.1", Integer.MIN_VALUE, Integer.MAX_VALUE));
		}

	}

}
