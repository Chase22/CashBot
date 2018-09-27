package org.chase.telegram.cashbot.Account;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccountsByChatId(String groupId) {
        return accountRepository
                .findAllByAccountIdentityGroupId(groupId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }

    public List<Account> getAccountsByUserId(long userId) {
        return accountRepository
                .findAllByAccountIdentityUserId(userId)
                .stream()
                .map(Account::new)
                .collect(Collectors.toList());
    }
}
