package org.chase.telegram.cashbot.Account;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Account {
    private long groupId;
    private int userId;
    private int balance;

    Account(final AccountEntity cashChatEntity) {
        groupId = cashChatEntity.getAccountIdentity().getGroupId();
        userId = cashChatEntity.getAccountIdentity().getUserId();
        balance = cashChatEntity.getBalance();
    }

    public void addToBalance(int amount) {
        this.setBalance(this.getBalance() + amount);
    }

    public void setBalance(int balance) {
        this.balance = balance;
        log.info("Changed Balance for acount in group {} for user {} from {} to {}",
                groupId, userId, this.balance, balance);
    }


}
