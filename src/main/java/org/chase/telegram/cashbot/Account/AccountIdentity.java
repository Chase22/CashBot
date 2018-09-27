package org.chase.telegram.cashbot.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
class AccountIdentity implements Serializable {
    private String groupId;
    private int userId;
}