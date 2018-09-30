package org.chase.telegram.cashbot.cashUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashUserRepository extends JpaRepository<CashUserEntity, Integer> {

    Optional<CashUserEntity> findByUserId(int userId);

    Optional<CashUserEntity> findByUsername(String username);
}
