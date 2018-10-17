package org.chase.telegram.cashbot.account.commands

import org.chase.telegram.cashbot.VerificationException
import org.chase.telegram.cashbot.account.Account
import org.chase.telegram.cashbot.account.AccountService
import org.chase.telegram.cashbot.commands.CashBotReply
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class AccountCashCommandClassSpec extends Specification {
    AccountService accountService = Mock(AccountService)
    AbsSender absSender = Mock(AbsSender)

    @Subject
    AccountCashCommand command = new AccountCashCommand("account", "","", accountService) {
        @Override
        protected Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) throws TelegramApiException {
            return Optional.empty()
        }

        @Override
        def int getExtraArgumentCount() {
            return 0
        }
    }

    @Unroll
    def "calling verify from a #type chat should throw an exception"() {
        given:
        Message message = new Message(chat:new Chat(type:chatType))

        when:
        command.verify(absSender, message, [] as String[])

        then:
        VerificationException e = thrown()
        e.message == "This command can only be used in groups"

        where:
        chatType << ["private", "channel"]
    }

    @Unroll
    def "calling verify with less than 2 arguments should throw an exception"() {
        accountService.getAccount(*_) >> Optional.of(new Account())

        given:
        Message message = new Message(chat:new Chat(type:"group"))

        when:
        command.verify(absSender, message, ["test"] as String[])

        then:
        VerificationException e = thrown()
        e.message == "Not enough arguments provided. Either add a username or reply to the message of a User you want to transfer money to"

        where:
        arguments << [[], ["test"]]
    }
}
