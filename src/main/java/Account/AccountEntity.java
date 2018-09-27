package Account;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashchat")
@Table(name = "chat")
@Data
public class AccountEntity {
    @EmbeddedId
    AccountIdentity accountIdentity;

    private int balance;
}
