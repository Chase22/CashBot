package org.chase.telegram.cashbot.cashChat.commands;

import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.chase.telegram.cashbot.cashUser.CashUser;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.HelpCategory;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@EnableCommand
@Component
public class ShowConfig extends CashCommand {
    private static final String IDENTIFIER = "showConfig";
    private static final String DESCRIPTION = "Shows the current Config";
    private static final String EXTENDED_DESCRIPTION = "Shows the current Config for the group";
    private final CashChatService cashChatService;
    private CashUserService cashUserService;

    public ShowConfig(final CashChatService cashChatService, final CashUserService cashUserService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.cashChatService = cashChatService;
        this.cashUserService = cashUserService;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        Optional<CashUser> cashUser = cashUserService.getById(message.getFrom().getId());

        if (cashUser.isPresent()) {
            return cashChatService.getById(message.getChatId())
                    .map(cashChat -> {
                        final CashBotReply cashBotReply = new CashBotReply(cashUser.get().getChatId(), cashChat.toChatString());
                        cashBotReply.setOriginChat(message.getChatId());
                        return Optional.of(cashBotReply);
                    })
                    .orElse(Optional.of(new CashBotReply(message.getChatId(), "The bot is not running for this chat")));
        }
        return Optional.of(new CashBotReply(message.getChatId(), "Please send /start to the bot in a private chat"));
    }

    @Override
    public Set<HelpCategory> getCategory() {
        return Collections.singleton(HelpCategory.Config);
    }
}
