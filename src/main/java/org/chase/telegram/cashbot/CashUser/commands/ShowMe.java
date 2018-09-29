package org.chase.telegram.cashbot.CashUser.commands;

import org.chase.telegram.cashbot.CashUser.CashUser;
import org.chase.telegram.cashbot.CashUser.CashUserService;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.VerifierService;
import org.chase.telegram.cashbot.commands.CashBotReply;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class ShowMe extends CashCommand {
    private static final String IDENTIFIER = "showMe";
    private static final String DESCRIPTION = "";
    private static final String EXTENDED_DESCRIPTION = "";

    private final CashUserService cashUserService;

    public ShowMe(final CashUserService cashUserService, final VerifierService verifierService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION, verifierService);
        this.cashUserService = cashUserService;
    }

    @Override
    protected void verify(final User user, final Chat chat, final String[] arguments, final VerifierService verifierService) throws VerificationException {
        cashUserService.getById(user.getId()).orElseThrow(() -> {
            return new VerificationException("You are not registered with the bot");
        });
    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final User user, final Chat chat, final String[] arguments) throws TelegramApiException {
        CashUser cashUser = cashUserService.getById(user.getId()).get();
        return Optional.of(new CashBotReply(chat.getId(),
                "Name: %s %s %n" +
                        "Username: %s %n" +
                        "UserId: %s %n",
                cashUser.getFirstName(), cashUser.getLastName(), cashUser.getUsername(), cashUser.getUserId()));
    }
}
