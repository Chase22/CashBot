package org.chase.telegram.cashbot.cashUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "cashuser")
@Table(name = "cashbot_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashUserEntity{

    @Id
    private int userId;
    private long chatId;
    private String username;
    private String firstName;
    private String lastName;
}
