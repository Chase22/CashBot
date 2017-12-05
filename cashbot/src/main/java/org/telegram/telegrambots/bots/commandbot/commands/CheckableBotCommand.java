package org.telegram.telegrambots.bots.commandbot.commands;

import java.util.ArrayList;
import java.util.List;

import org.chase.telegram.cashbot.commands.base.BotCommandFilter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.DefaultBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public abstract class CheckableBotCommand extends DefaultBotCommand {

	private List<BotCommandFilter> filters = new ArrayList<>();
	
	protected SendMessage reply;

	public CheckableBotCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	public boolean canExecute(AbsSender absSender, User user, Chat chat, String[] arguments, boolean supressMessage) {
		for (BotCommandFilter filter : filters) {
			if (!filter.filter(absSender, user, chat, arguments, supressMessage)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public final void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {}

	@Override
	void processMessage(AbsSender absSender, Message message, String[] arguments) {
		if (canExecute(absSender, message.getFrom(), message.getChat(), arguments, false)) {
			reply = new SendMessage(message.getChatId(), "");
			executeCommand(absSender, message.getFrom(), message.getChat(), message, arguments);
			try {
				if (!reply.getText().isEmpty()) {
					absSender.execute(reply);
				}
			} catch (TelegramApiException e) {
				BotLogger.error(this.getClass().getSimpleName(), e);
			}
		}
		
	}
	

	public abstract void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments);

	public boolean addFilter(BotCommandFilter filter) {
		return filters.add(filter);
	}

	public boolean removeFilter(BotCommandFilter filter) {
		return filters.remove(filter);
	}
}
