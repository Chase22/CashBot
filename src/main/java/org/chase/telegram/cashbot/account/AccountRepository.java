package org.chase.telegram.cashbot.account;

import org.chase.telegram.cashbot.GroupUserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, GroupUserIdentifier> {
    List<AccountEntity> findAllByGroupUserIdentifierGroupId(long groupId);
    List<AccountEntity> findAllByGroupUserIdentifierUserId(int userId);

    Optional<AccountEntity> findByGroupUserIdentifier(GroupUserIdentifier groupUserIdentifier);
}
