package org.chase.telegram.cashbot.cashUser;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CashUserService {
    private final CashUserRepository cashUserRepository;

    public CashUserService(final CashUserRepository cashUserRepository) {
        this.cashUserRepository = Objects.requireNonNull(cashUserRepository, "cashUserRepository");
    }

    public void getAndUpdateUser(final User user, final long chatId) {
        getById(user.getId()).ifPresent(cashUser -> {
            CashUser newUser = new CashUser(user, chatId);
            if (newUser.equals(cashUser)) {
                return;
            }
            save(newUser);
        });
    }

    public Optional<CashUser> getById(int userId) {
        return cashUserRepository.findByUserId(userId).map(CashUser::new);
    }

    public List<CashUser> getForChatId(String chatId) {
        return null;
    }

    public CashUserEntity save(final CashUser cashUser) {
        return cashUserRepository.save(cashUser.toEntity());
    }

    public Optional<CashUser> getByUsername(final String username) {
        final String usernameSanitized = username.replace("@", "").toLowerCase();

        return cashUserRepository.findByUsernameIgnoreCase(usernameSanitized).map(CashUser::new);
    }

    public void delete(final CashUser cashUser) {
        cashUserRepository.delete(cashUser.toEntity());
    }
}
