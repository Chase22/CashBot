package org.chase.telegram.cashbot.commands

import org.chase.telegram.cashbot.AbstractSpecification
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

    private final CashChat someCashChat = new CashChat(-50000, "", 0, 0, 0, 0, 0, 0, "")
    private final Chat someChat = new Chat(id: -50000, type: "group", title: "someTitle")
    private User someUser = new User(1000, "someName", false, "someLastName", "someUserName", "en")
    private Message someMessage = new Message(chat: someChat, from: someUser)


    def "setup"() {
        telegramUserRightService.isAdministrator(_) >> false

        commandGroup = new StartCommandGroup(cashChatService)
    }

    def "calling executeCommand with a chat that already exists should throw an exception"() {
        given:
        telegramUserRightService.isAdministrator(*_) >> true

        when:
        CashBotReply reply = commandGroup.executeCommand(absSender, someMessage, arguments).get()

        then:
        reply.reply.text == "Bot is already running"
        1*cashChatService.getById(-50000) >> Optional.of(someCashChat)
    }

    def "if calling executeCommand as an admin without an existing group should not throw an excception"() {
        given:
        telegramUserRightService.isAdministrator(*_) >> true

        when:
        CashBotReply reply = commandGroup.executeCommand(absSender, someMessage, arguments).get()

        then:
        1 * cashChatService.getById(_) >> Optional.empty()
        0 * cashChatService.createDefault(_ as Long, _ as String) > Optional.of(someCashChat)
        assert true
    }

}
