package org.chase.telegram.cashbot.CashChat;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.Account.Account;
import org.chase.telegram.cashbot.Account.AccountService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class CashChatService {
    private final CashChatRepository cashChatRepository;
    private final AccountService accountService;

    public CashChatService(CashChatRepository cashChatRepository, AccountService accountService) {
        this.cashChatRepository = requireNonNull(cashChatRepository, "cashChatRepository");
        this.accountService = requireNonNull(accountService, "accountService");
    }

    public Optional<CashChat> getChatChatById(String chatId) {
        return cashChatRepository.findByChatID(chatId).map(CashChat::new);
    }

    public void handleMessage(Message message) {
        getChatChatById(message.getChatId().toString()).ifPresent(chat -> {
            accountService.getAccount(message.getFrom().getId(), chat.getChatId()).ifPresent(account -> {
                if (message.hasText()) {
                    account.addToBalance(chat.getAmountText());
                } else if (message.hasPhoto()) {
                    account.addToBalance(chat.getAmountPic());
                } else if (message.getVoice() != null) {
                    account.addToBalance(chat.getAmountVoice());
                } else if (message.hasSticker()) {
                    account.addToBalance(chat.getAmountSticker());
                } else {
                    account.addToBalance(chat.getAmountOther());
                }
            });
        });

    }
}
