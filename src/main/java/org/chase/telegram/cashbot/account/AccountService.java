package org.chase.telegram.cashbot.account;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.ChatNotRegisteredException;
import org.chase.telegram.cashbot.GroupUserIdentifier;
import org.chase.telegram.cashbot.cashChat.CashChat;
import org.chase.telegram.cashbot.cashChat.CashChatService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final CashChatService cashChatService;

    public AccountService(AccountRepository accountRepository, final CashChatService cashChatService) {
        this.accountRepository = requireNonNull(accountRepository, "accountRepository");
        this.cashChatService = requireNonNull(cashChatService, "cashChatService");
    }

    public List<Account> getAccountsByChatId(long groupId) {
        return accountRepository
                .findAllByGroupUserIdentifierGroupId(groupId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public List<Account> getAccountsByUserId(int userId) {
        return accountRepository
                .findAllByGroupUserIdentifierUserId(userId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public Optional<Account> getAccount(int userId, long groupId) {
        return accountRepository.findByGroupUserIdentifier(new GroupUserIdentifier(groupId, userId)).map(Account::new);
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

    public void transferTo(Account from, Account to, int amount) throws AccountException {
        if (amount < 0) {
            throw new AccountException("Amount can not be negative");
        }

        if (from.getBalance() < amount) {
            throw new AccountException("Balance not sufficient");
        }

        from.addToBalance(amount*-1);
        to.addToBalance(amount);

        accountRepository.save(from.toEntity());
        accountRepository.save(to.toEntity());
    }

    public void handleMessage(Message message) {
        cashChatService.getById(message.getChatId()).ifPresent(chat -> {
            log.debug("found chat: {}", chat);
            getAccount(message.getFrom().getId(), chat.getChatId()).ifPresent(account -> {
                log.debug("found account: {}", account);
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
                accountRepository.save(account.toEntity());
            });
        });

    }

    public void deleteAccount(final Account account) {
        accountRepository.delete(account.toEntity());
    }

    public void saveAccount(final Account account) {
        accountRepository.save(account.toEntity());
    }
}
