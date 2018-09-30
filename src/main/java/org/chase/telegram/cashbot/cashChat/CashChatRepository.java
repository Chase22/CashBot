package org.chase.telegram.cashbot.cashChat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashChatRepository extends JpaRepository<CashChatEntity, Long> {
    Optional<CashChatEntity> findByChatId(Long chatId);
}
