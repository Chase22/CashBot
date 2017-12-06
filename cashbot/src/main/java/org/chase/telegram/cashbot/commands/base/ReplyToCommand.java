package org.chase.telegram.cashbot.commands.base;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;

public abstract class ReplyToCommand extends CheckableBotCommand {

	public ReplyToCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public final void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		Integer userID = -1;
		String[] newArgs = arguments;
		if (message.isReply()) {
			userID = message.getReplyToMessage().getFrom().getId();
		} else if (arguments.length >= 1) {
			EntityManager manager = EntityUtility.getEntityManager();
			userID = CashUser.getIdByUsername(manager, arguments[0].replaceFirst("@", ""));
			newArgs = Arrays.copyOfRange(newArgs, 1, newArgs.length);
			if (userID == -1) {
				reply.setText(Messages.getFormatString("ReplyToCommand.0", arguments[0]));
			}
		} else {
			reply.setText(Messages.getString("ReplyToCommand.1"));
		}

		if (reply.getText().isEmpty()) {
			executeCommand(absSender, user, chat, message, newArgs, userID);
		}
	}

	public abstract void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments,
			Integer repliedToID);

}
