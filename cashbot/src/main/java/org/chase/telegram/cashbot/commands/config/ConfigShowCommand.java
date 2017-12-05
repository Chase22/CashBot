package org.chase.telegram.cashbot.commands.config;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.commands.base.filter.CashChatCommandFilter;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class ConfigShowCommand extends CheckableBotCommand {

	private static final String IDENTIFIER = "configShow"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("ConfigShowCommand.1"); //$NON-NLS-1$

	EntityManager manager;

	public ConfigShowCommand() {
		super(IDENTIFIER, DESCRIPTION);
		addFilter(new CashChatCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		manager = EntityUtility.getEntityManager();
		SendMessage reply = new SendMessage().setChatId(chat.getId());
		CashChat cashChat = CashChat.load(manager, chat.getId());
		StringBuilder text = new StringBuilder();
		text.append(Messages.getString("ConfigShowCommand.2")).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.3")).append(cashChat.getCurrencyName()).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.4")).append(cashChat.getStartAmount()).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.5")).append(cashChat.getAmountText()).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.6")).append(cashChat.getAmountSticker()).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.7")).append(cashChat.getAmountPic()).append(System.lineSeparator()); //$NON-NLS-1$
		text.append(Messages.getString("ConfigShowCommand.8")).append(cashChat.getAmountVoice()).append(System.lineSeparator()); //$NON-NLS-1$
		reply.setText(text.toString());

		if (reply.getText() != null) {
			try {
				absSender.execute(reply);
			} catch (TelegramApiException e) {
				BotLogger.error(this.getClass().getSimpleName(), e);
			}
		}
		manager.close();
	}

}
