package org.chase.telegram.cashbot.session;

import org.chase.telegram.cashbot.GroupUserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, GroupUserIdentifier> {

    Optional<SessionEntity> findByGroupUserIdentifier_GroupIdAndGroupUserIdentifier_UserId(long groupId, int userId);
}
