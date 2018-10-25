package org.chase.telegram.cashbot.commands

import org.chase.telegram.cashbot.AbstractSpecification
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

    def "executeCommand should return an errorReply if called with type channel"() {
        given:
        Chat chat = new Chat(type: "channel", id: 1000)
        Message message = new Message(chat: chat)

        when:
            CashBotReply reply = startCommand.executeCommand(absSender, message, arguments).get()
        then:
            reply.reply.text == "This bot can only be used in Private or Group chats"
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
