package org.chase.telegram.cashbot.flags;

import org.chase.telegram.cashbot.GroupUserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlagRepository extends JpaRepository<FlagEntity, GroupUserIdentifier> {
    Optional<FlagEntity> getByGroupUserIdentifier(GroupUserIdentifier groupUserIdentifier);
}
