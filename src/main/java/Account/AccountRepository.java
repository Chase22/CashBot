package Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, AccountIdentity> {
    List<AccountEntity> findAllByAccountIdentityChatId(String chatId);
    List<AccountEntity> findAllByAccountIdentityUserId(long userId);
}
