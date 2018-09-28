package org.chase.telegram.cashbot.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, AccountIdentity> {
    List<AccountEntity> findAllByAccountIdentityGroupId(long groupId);
    List<AccountEntity> findAllByAccountIdentityUserId(int userId);

    Optional<AccountEntity> findByAccountIdentity(AccountIdentity accountIdentity);
}
