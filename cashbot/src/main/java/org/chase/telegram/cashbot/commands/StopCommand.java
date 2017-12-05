package org.chase.telegram.cashbot.commands;

import org.chase.telegram.cashbot.Main;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;

public class StopCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "stop"; //$NON-NLS-1$
	private static final String DESCRIPTION = ""; //$NON-NLS-1$

	public StopCommand() {
		super(IDENTIFIER, DESCRIPTION);
	}

	
	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		if (user.getId() == 188215327) {
			Main.cashBotSession.stop();
		}
	}

}
