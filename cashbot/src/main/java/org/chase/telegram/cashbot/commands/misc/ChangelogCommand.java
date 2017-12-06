package org.chase.telegram.cashbot.commands.misc;

import java.util.Map.Entry;

import org.chase.telegram.cashbot.Changelog;
import org.chase.telegram.cashbot.FormatException;
import org.chase.telegram.cashbot.Messages;
import org.chase.telegram.cashbot.Version;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class ChangelogCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "changelog"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("ChangelogCommand.1"); //$NON-NLS-1$

	public ChangelogCommand() {
		super(IDENTIFIER, DESCRIPTION);
	}

	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		SendMessage reply = new SendMessage(chat.getId(), "");
		reply.setParseMode(ParseMode.MARKDOWN);

		try {
			if (arguments.length > 0) {
				if (Version.Validate(arguments[0])) {
					reply.setText(Changelog.getInstance().get(new Version(arguments[0])).toString());
				} else if (arguments[0].equals("all")) {
					for (Entry<Version, Changelog> log : Changelog.getInstance().entrySet()) {
						reply.setText(log.getValue().toString());
						absSender.execute(reply);
					}
					return;
				}
			} else {
				reply.setText(Changelog.getLatest().toString());
			}

			if (!reply.getText().isEmpty()) absSender.execute(reply);
		} catch (TelegramApiException | FormatException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}

}
