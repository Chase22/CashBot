package org.chase.telegram.cashbot.CashChat;

import org.springframework.stereotype.Service;

@Service
public class CashChatService {
    private final CashChatRepository cashChatRepository;

    public CashChatService(CashChatRepository cashChatRepository) {
        this.cashChatRepository = cashChatRepository;
    }

    public CashChat getChatChatById(String chatId) {
        return new CashChat(cashChatRepository.getOne(chatId));
    }
}
