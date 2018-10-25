package org.chase.telegram.cashbot.commands

import org.chase.telegram.cashbot.AbstractSpecification
import org.chase.telegram.cashbot.VerificationException
import org.chase.telegram.cashbot.cashChat.CashChatService
import org.chase.telegram.cashbot.cashUser.CashUser
import org.chase.telegram.cashbot.cashUser.CashUserService
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import spock.lang.Subject

class StartCommandUserTest extends AbstractSpecification {
    AbsSender absSender = Mock(AbsSender)
    CashChatService cashChatService = Mock(CashChatService)
    CashUserService cashUserService = Mock(CashUserService)

    String[] arguments = []

    @Subject(StartCommandUser)
    StartCommandUser commandUser


    def "setup"() {
        commandUser = new StartCommandUser(cashUserService)
    }

    def "calling verify with a user that is already registered should throw an exception"() {
        given:
        User someUser = new User(1000, "someName", false, "someLastName", "someUserName", "en")
        Chat someChat = new Chat(id: -5000, type: "user", title: "someTitle")
        Message someMessage = new Message(chat: someChat, from: someUser)

        when:
        commandUser.verify(absSender, someMessage, arguments)

        then:
        VerificationException e = thrown(VerificationException)
        e.message == "Bot is already running"
        1*cashUserService.getById(someUser.id) >> Optional.of(new CashUser(1000, -5000, "username", "firstName", "lastName"))
    }

}
