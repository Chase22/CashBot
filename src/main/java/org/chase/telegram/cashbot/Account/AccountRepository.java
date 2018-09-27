package org.chase.telegram.cashbot.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, AccountIdentity> {
    List<AccountEntity> findAllByAccountIdentityGroupId(String groupId);
    List<AccountEntity> findAllByAccountIdentityUserId(long userId);
}
