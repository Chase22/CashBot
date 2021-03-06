package org.chase.telegram.cashbot.account;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Account {
    private long groupId;
    private int userId;
    private int balance;

    Account(final AccountEntity cashChatEntity) {
        groupId = cashChatEntity.getGroupUserIdentifier().getGroupId();
        userId = cashChatEntity.getGroupUserIdentifier().getUserId();
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


    AccountEntity toEntity() {
        return new AccountEntity(groupId, userId, balance);
    }
}
