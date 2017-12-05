package org.chase.telegram.cashbot.commands.config;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;

import org.chase.telegram.cashbot.commands.base.filter.AdminCommandFilter;
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

public class ConfigSetCommand extends CheckableBotCommand {

	CashChat.Configs config;

	public ConfigSetCommand(CashChat.Configs config) {

		super("configSet" + config.name(), Messages.getFormatString("ConfigSetCommand.1", config.name())); //$NON-NLS-1$ //$NON-NLS-2$
		this.config = config;

		addFilter(new AdminCommandFilter());
		addFilter(new CashChatCommandFilter());
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		EntityManager manager = EntityUtility.getEntityManager();

		CashChat cashChat = CashChat.load(manager, chat.getId());
		SendMessage reply = new SendMessage().setChatId(chat.getId());
		try {
			if (arguments.length == 0) {
				reply.setText(Messages.getFormatString("ConfigSetCommand.2", getCommandIdentifier())); //$NON-NLS-1$
			} else {
				if (cashChat == null) {
					reply.setText(Messages.getString("ConfigSetCommand.3")); //$NON-NLS-1$
				} else {
					cashChat.setConfig(manager, config, arguments[0]);
					reply.setText(Messages.getFormatString("ConfigSetCommand.4", config.name(), arguments[0])); //$NON-NLS-1$
				}
			}

			if (reply.getText() != null) {
				absSender.execute(reply);
			}

		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
		manager.close();

	}

}
