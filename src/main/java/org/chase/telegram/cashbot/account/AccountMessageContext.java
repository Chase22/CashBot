package org.chase.telegram.cashbot.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountMessageContext {
    private Account fromAccount;
    private Account toAccount;
    private Integer amount;

}
