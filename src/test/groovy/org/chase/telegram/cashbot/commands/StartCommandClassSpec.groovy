package org.chase.telegram.cashbot.commands

import org.chase.telegram.cashbot.AbstractSpecification
import org.chase.telegram.cashbot.VerificationException
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import spock.lang.Subject
import spock.lang.Unroll

class StartCommandClassSpec extends AbstractSpecification {

    StartCommandGroup commandGroup = Mock(StartCommandGroup)
    StartCommandUser commandUser = Mock(StartCommandUser)
    AbsSender absSender = Mock(AbsSender)

    @Subject(StartCommand)
    StartCommand startCommand
    String[] arguments = []

    def "setup"() {
        startCommand = new StartCommand(commandGroup, commandUser)
    }

    def "verify should throw an exception if the given chat is a channel"() {
        given:
        Chat chat = new Chat(type: "channel")
        Message message = new Message(chat: chat)
        when:
            startCommand.verify(absSender, message, arguments)
        then:
            thrown(VerificationException)
    }

    @Unroll
    def "verify should call StartCommandGroup if the chat type is #type"(String type) {
        given:
        Chat chat = new Chat(type: type)
        Message message = new Message(chat: chat)

        when:
        startCommand.verify(absSender, message, arguments)

        then:
        1*commandGroup.verify(absSender, message, arguments)

        where:
        type << ["group", "supergroup"]
    }

    def "verify should call StartCommandUser if the chat type is private"() {
        given:
        Chat chat = new Chat(type: "private")
        Message message = new Message(chat: chat)

        when:
        startCommand.verify(absSender, message, arguments)

        then:
        1*commandUser.verify(absSender, message, arguments)

    }
    def "executeCommand should not call any other classes if the given chat is a channel"() {
        given:
        Chat chat = new Chat(type: "channel")
        Message message = new Message(chat: chat)
        when:
            startCommand.executeCommand(absSender, message, arguments)
        then:
            0*commandUser.executeCommand(absSender, message, arguments)
            0*commandGroup.executeCommand(absSender, message, arguments)
    }

    @Unroll
    def "executeCommand should call StartCommandGroup if the chat type is #type"(String type) {
        given:
        Chat chat = new Chat(type: type)
        Message message = new Message(chat: chat)

        when:
        startCommand.executeCommand(absSender, message, arguments)

        then:
        1*commandGroup.executeCommand(absSender, message, arguments)

        where:
        type << ["group", "supergroup"]
    }

    def "execute should call StartCommandUser if the chat type is private"() {
        given:
        Chat chat = new Chat(type: "private")
        Message message = new Message(chat: chat)

        when:
        startCommand.executeCommand(absSender, message, arguments)

        then:
        1*commandUser.executeCommand(absSender, message, arguments)

    }
}
