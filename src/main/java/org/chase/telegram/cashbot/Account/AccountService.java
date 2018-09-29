package org.chase.telegram.cashbot.Account;

import org.chase.telegram.cashbot.CashChat.CashChat;
import org.chase.telegram.cashbot.CashChat.CashChatService;
import org.chase.telegram.cashbot.ChatNotRegisteredException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CashChatService cashChatService;

    public AccountService(AccountRepository accountRepository, final CashChatService cashChatService) {
        this.accountRepository = requireNonNull(accountRepository, "accountRepository");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    public List<Account> getAccountsByChatId(long groupId) {
        return accountRepository
                .findAllByAccountIdentityGroupId(groupId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public List<Account> getAccountsByUserId(int userId) {
        return accountRepository
                .findAllByAccountIdentityUserId(userId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public Optional<Account> getAccount(int userId, long groupId) {
        return accountRepository.findByAccountIdentity(new AccountIdentity(groupId, userId)).map(Account::new);
    }

    public Account createNew(final Integer userId, final Long chatId) {
        Optional<CashChat> optionalCashChat = cashChatService.getById(chatId);
        if (optionalCashChat.isPresent()) {
            CashChat cashChat = optionalCashChat.get();
            AccountEntity accountEntity = new AccountEntity(chatId, userId, cashChat.getStartAmount());
            return new Account(accountRepository.save(accountEntity));
        } else {
            throw new ChatNotRegisteredException();
        }
    }

    public void handleMessage(Message message) {
        cashChatService.getById(message.getChatId()).ifPresent(chat -> {
            getAccount(message.getFrom().getId(), chat.getChatId()).ifPresent(account -> {
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
