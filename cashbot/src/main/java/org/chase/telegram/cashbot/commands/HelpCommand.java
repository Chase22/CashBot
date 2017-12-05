package org.chase.telegram.cashbot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.chase.telegram.cashbot.Messages;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class HelpCommand extends BotCommand {
	private static final String IDENTIFIER = "help"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("HelpCommand.1"); //$NON-NLS-1$

	public HelpCommand() {
		super(IDENTIFIER, DESCRIPTION);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		StringBuilder reply = new StringBuilder();
		TelegramLongPollingCommandBot sender = (TelegramLongPollingCommandBot) absSender;
		List<BotCommand> commands = new ArrayList<>(sender.getRegisteredCommands());
		Collections.sort(commands, new Comparator<BotCommand>() {
			@Override
			public int compare(BotCommand o1, BotCommand o2) {
				return o1.getCommandIdentifier().compareTo(o2.getCommandIdentifier());
			}
		});
		for (BotCommand b : commands) {
			if (b.getDescription() == "") //$NON-NLS-1$
				continue;

			if (b instanceof CheckableBotCommand) {
				CheckableBotCommand c = (CheckableBotCommand) b;
				if (!c.canExecute(absSender, user, chat, arguments, true)) {
					continue;
				}
			}
			reply.append(Messages.getFormatString("HelpCommand.2", b.getCommandIdentifier(), b.getDescription())); //$NON-NLS-1$
		}
		try {
			sender.execute(new SendMessage(chat.getId(), reply.toString()));
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}

}
