package org.chase.telegram.cashbot.cashChat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class CashChatService {
    private final CashChatRepository cashChatRepository;

    public CashChatService(final CashChatRepository cashChatRepository) {
        this.cashChatRepository = requireNonNull(cashChatRepository, "cashChatRepository");
    }

    public CashChat createDefault(long chatId, String chatTitle) {
        CashChatEntity newChat = new CashChatEntity(
                chatId,
                chatTitle,
                10,
                10,
                10,
                10,
                10,
                10,
                "Muns");
        cashChatRepository.save(newChat);
        return new CashChat(newChat);
    }

    public Optional<CashChat> getById(long chatId) {
        return cashChatRepository.findByChatId(chatId).map(CashChat::new);
    }

    public CashChatEntity save(final CashChat cashChat) {
        return cashChatRepository.save(cashChat.toEntity());
    }
}
