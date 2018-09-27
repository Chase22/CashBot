package Account;

import lombok.Data;

import javax.persistence.Id;

@Data
class AccountIdentity {
    @Id
    private long groupId;
    @Id
    private int userId;
}