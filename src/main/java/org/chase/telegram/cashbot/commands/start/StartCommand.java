package org.chase.telegram.cashbot.commands.start;

import com.google.common.collect.Sets;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.HelpCategory;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Component
@EnableCommand
public class StartCommand extends CashCommand {
	private static final String IDENTIFIER = "start";
	private static final String DESCRIPTION = "Starts the bot";
	private static final String EXTENDED_DESCRIPTION = "Starts the bot in this chat. \n " +
            "if used in a group chat, it will register the group with the bot and make it available for use. \n " +
            "if used in a private chat it will register you with the bot, which makes it possible for you to use the bot";

    private final StartCommandGroup startCommandGroup;
    private final StartCommandUser startCommandUser;

    public StartCommand(final StartCommandGroup startCommandGroup, final StartCommandUser startCommandUser) {
		super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);
        this.startCommandGroup = requireNonNull(startCommandGroup, "startCommandGroup");
        this.startCommandUser = requireNonNull(startCommandUser, "startCommandUser");
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException{
        final Chat chat = message.getChat();

        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            return startCommandGroup.executeCommand(absSender, message, arguments);
        } else if (chat.isUserChat()) {
            return startCommandUser.executeCommand(absSender, message, arguments);
        }
        return Optional.of(new CashBotReply(message.getChatId(), "This bot can only be used in Private or Group chats"));
    }

    @Override
	public String getExtendedDescription() {
		return EXTENDED_DESCRIPTION;
	}

    @Override
    public Set<HelpCategory> getCategory() {
        return Sets.newHashSet(HelpCategory.Private, HelpCategory.Group);
    }
}
