package org.chase.telegram.cashbot.Account;

public class AccountNotFoundException extends Throwable {
    public AccountNotFoundException(final String format, final Object... args) {
        super(String.format(format, args));
    }
}
