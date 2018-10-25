package org.chase.telegram.cashbot.commands

import org.chase.telegram.cashbot.AbstractSpecification
import org.chase.telegram.cashbot.VerificationException
import org.chase.telegram.cashbot.bot.TelegramUserRightService
import org.chase.telegram.cashbot.cashChat.CashChat
import org.chase.telegram.cashbot.cashChat.CashChatService
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import spock.lang.Subject

class StartCommandGroupTest extends AbstractSpecification {
    AbsSender absSender = Mock(AbsSender)
    CashChatService cashChatService = Mock(CashChatService)
    TelegramUserRightService telegramUserRightService = Mock(TelegramUserRightService)

    String[] arguments = []

    @Subject(StartCommandGroup)
    StartCommandGroup commandGroup


    def "setup"() {
        telegramUserRightService.isAdministrator(_) >> false

        commandGroup = new StartCommandGroup(cashChatService)
    }

    def "calling verify with a user that isn't an admin should throw an exception"() {
        given:

        User someUser = new User(1000, "someName", false, "someLastName", "someUserName", "en")
        Chat someChat = new Chat(id: -50000, type: "group", title: "someTitle")
        Message someMessage = new Message(chat: someChat, from: someUser)

        when:
        commandGroup.verify(absSender, someMessage, arguments)

        then:
        VerificationException e = thrown(VerificationException)
        e.message == "This command can only be executed by Admins"
        1*cashChatService.getById(someChat.id) >> Optional.empty()
    }

    def "calling verify with a chat that already exists should throw an exception"() {
        given:
        telegramUserRightService.isAdministrator(*_) >> true

        User someUser = new User(1000, "someName", false, "someLastName", "someUserName", "en")
        Chat someChat = new Chat(id: -50000, type: "group", title: "someTitle")
        Message someMessage = new Message(chat: someChat, from: someUser)

        when:
        commandGroup.verify(absSender, someMessage, arguments)

        then:
        VerificationException e = thrown(VerificationException)
        e.message == "Bot is already running"
        1*cashChatService.getById(-50000) >> Optional.of(new CashChat(-50000, "", 0,0,0,0,0,0,""))
    }

    def "if calling verify as an admin without an existing group should not throw an excception"() {
        given:
        telegramUserRightService.isAdministrator(*_) >> true
        cashChatService.getById(_) >> Optional.empty()

        User someUser = new User(1000, "someName", false, "someLastName", "someUserName", "en")
        Chat someChat = new Chat(id: -50000, type: "group", title: "someTitle")
        Message someMessage = new Message(chat: someChat, from: someUser)

        when:
        commandGroup.verify(absSender, someMessage, arguments)

        then:
        noExceptionThrown()
    }

}
