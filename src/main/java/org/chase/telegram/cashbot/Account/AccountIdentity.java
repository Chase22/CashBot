package org.chase.telegram.cashbot.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
class AccountIdentity implements Serializable {
    private String groupId;
    private int userId;
}