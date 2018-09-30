package org.chase.telegram.cashbot.commands;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.VerificationException;
import org.chase.telegram.cashbot.changelog.Changelog;
import org.chase.telegram.cashbot.changelog.ChangelogService;
import org.chase.telegram.cashbot.changelog.FormatException;
import org.chase.telegram.cashbot.changelog.Version;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Component
@EnableCommand
public class ChangelogCommand extends CashCommand {
    private static final String IDENTIFIER = "changelog";
    private static final String DESCRIPTION = "gets the changelog for the bot";
    private static final String EXTENDED_DESCRIPTION = "Displays the latest changelog.\n use /changelog [VersionNumber] to get a certain version or /changelog all to get all changelogs";

    private final ChangelogService changelogService;

    public ChangelogCommand(final ChangelogService changelogService) {
        super(IDENTIFIER, DESCRIPTION, EXTENDED_DESCRIPTION);

        this.changelogService = requireNonNull(changelogService, "changelogService");
    }

    @Override
    protected void verify(final Message message, final String[] arguments, final AbsSender absSender) throws VerificationException {

    }

    @Override
    protected Optional<CashBotReply> executeCommand(final AbsSender absSender, Message message, final String[] arguments) {
        try {
            if (arguments.length > 0) {
                if (Version.Validate(arguments[0])) {
                    CashBotReply reply = new CashBotReply(message.getChatId(), changelogService.get(new Version(arguments[0])).toString());
                    reply.getReply().setParseMode(ParseMode.MARKDOWN);
                    return Optional.of(reply);
                } else if (arguments[0].equals("all")) {
                    for (Map.Entry<Version, Changelog> log : changelogService.getChangelogs().entrySet()) {
                        absSender.execute(new SendMessage(message.getChatId(), log.getValue().toString()));
                    }
                }
            } else {
                CashBotReply reply = new CashBotReply(message.getChatId(), changelogService.getLatest().toString());
                reply.getReply().setParseMode(ParseMode.MARKDOWN);
                return Optional.of(reply);
            }
        } catch (TelegramApiException | FormatException e) {
            log.error("Error sending Changelogs", e);
            return Optional.of(new CashBotReply(message.getChatId(), "Error sending Changelogs"));
        }
        return Optional.empty();
    }
}