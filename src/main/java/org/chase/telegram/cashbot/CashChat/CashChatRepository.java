package org.chase.telegram.cashbot.CashChat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashChatRepository extends JpaRepository<CashChatEntity, String> {
    Optional<CashChatEntity> findByChatId(String chatId);
}
