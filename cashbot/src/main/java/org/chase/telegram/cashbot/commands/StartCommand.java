package org.chase.telegram.cashbot.commands;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.bot.GroupUtils;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class StartCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "start"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("StartCommand.1"); //$NON-NLS-1$

	public StartCommand() {
		super(IDENTIFIER, DESCRIPTION);
	}
	
	@Override
	public boolean canExecute(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage) {
		EntityManager manager = EntityUtility.getEntityManager();
		if (super.canExecute(absSender, user, chat, arguments, supressMessage)) {
			if (chat.isChannelChat()) {
				return false;
			} else if (chat.isUserChat()) {
				CashUser cashUser = CashUser.load(manager, user.getId());
				return (cashUser == null || cashUser.getChatID() != 0);
			} else {
				CashChat cashChat = CashChat.load(manager, chat.getId());
				return cashChat == null;
			}
		} else {
			return false;
		}
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		SendMessage reply = new SendMessage();
		EntityManager manager = EntityUtility.getEntityManager();
		
		reply.setChatId(chat.getId());
		if (chat.isGroupChat() || chat.isSuperGroupChat()) {
			CashChat cashChat = CashChat.load(manager, chat.getId());
			if (cashChat == null) {
				try {
					if (GroupUtils.isAdministrator(absSender, chat, user)) {
						cashChat = CashChat.createDefault(manager, chat.getId());
						reply.setText(Messages.getString("StartCommand.2")); //$NON-NLS-1$
					} else {
						reply.setText(Messages.getString("StartCommand.3")); //$NON-NLS-1$
					}
				} catch (TelegramApiException e) {
					BotLogger.error(this.getClass().getSimpleName(), e);
				}
			}
		} else if (chat.isUserChat()) {
			CashUser cashUser = CashUser.load(manager, user.getId());
			if (cashUser == null) {
				cashUser = CashUser.create(EntityUtility.getEntityManager(), user.getId(), chat.getId(),
						chat.getUserName(), user.getFirstName(), user.getLastName());
				reply.setText(Messages.getString("StartCommand.4")); //$NON-NLS-1$
			} else if (cashUser.getChatID() == 0) {
				cashUser.setChatID(EntityUtility.getEntityManager(), chat.getId());
				reply.setText(Messages.getString("StartCommand.5")); //$NON-NLS-1$
			} else {
				reply.setText(Messages.getString("StartCommand.6")); //$NON-NLS-1$
			}
		} else {
			reply.setText(Messages.getString("StartCommand.7")); //$NON-NLS-1$
		}
		manager.close();
		try {
			if (reply.getText() != null && !reply.getText().isEmpty()) {
				absSender.execute(reply);
			}

		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}

}
