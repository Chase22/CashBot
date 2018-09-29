package org.chase.telegram.cashbot.flags;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlagRepository extends JpaRepository<FlagEntity, FlagIdentifier> {
    Optional<FlagEntity> getByFlagIdentifier(FlagIdentifier flagIdentifier);
}
