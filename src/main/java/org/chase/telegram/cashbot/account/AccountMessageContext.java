package org.chase.telegram.cashbot.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountMessageContext {
    private Account fromAccount;
    private Account toAccount;
    private Integer amount;

    public Optional<Account> getFromAccount() {
        return Optional.ofNullable(fromAccount);
    }

}
