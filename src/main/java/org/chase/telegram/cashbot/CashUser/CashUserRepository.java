package org.chase.telegram.cashbot.CashUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashUserRepository extends JpaRepository<CashUserEntity, Long> {

    Optional<CashUserEntity> findByUserId(int userId);
}
