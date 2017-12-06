package org.chase.telegram.cashbot.commands.account;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.filter.CashUserCommandFilter;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class ShowAllAccountsCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "showAccounts"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("ShowAllAccountsCommand.0"); //$NON-NLS-1$

	public ShowAllAccountsCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new CashUserCommandFilter());

	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		EntityManager manager = EntityUtility.getEntityManager();

		StringBuilder replyBuilder = new StringBuilder();

		if (chat.isUserChat()) {
			CashUser cashUser = CashUser.load(manager, user.getId());

			if (cashUser.getAccounts().isEmpty()) {
				replyBuilder.append(Messages.getString("ShowAllAccountsCommand.1"));
			} else {
				for (Account acc : cashUser.getAccounts()) {
					try {
						CashChat cashChat = CashChat.load(manager, acc.getGroupID());
						GetChat getChat = new GetChat(cashChat.getChatID());
						Chat currentChat = absSender.execute(getChat);
						replyBuilder.append(currentChat.getTitle()).append(": ").append(acc.getBalance()).append(" ")
								.append(cashChat.getCurrencyName()).append(System.lineSeparator());
					} catch (TelegramApiException e) {
						BotLogger.error(this.getClass().getSimpleName(), e);
					}
				}
			}

		} else {
			CashChat cashChat = CashChat.load(manager, chat.getId());

			if (cashChat.getAccounts().isEmpty()) {
				replyBuilder.append(Messages.getString("ShowAllAccountsCommand.1"));
			} else {
				for (Account acc : cashChat.getAccounts()) {
					CashUser owner = CashUser.load(manager, acc.getUserID());
					replyBuilder.append(owner.getName()).append(": ").append(acc.getBalance()).append(" ")
							.append(cashChat.getCurrencyName()).append(System.lineSeparator());
				}
			}
		}

		SendMessage reply = new SendMessage(chat.getId(), replyBuilder.toString());

		try {
			absSender.execute(reply);
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}

	}
}
