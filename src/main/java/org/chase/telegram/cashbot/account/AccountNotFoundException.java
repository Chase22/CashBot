package org.chase.telegram.cashbot.account;

public class AccountNotFoundException extends Throwable {
    public AccountNotFoundException(final String format, final Object... args) {
        super(String.format(format, args));
    }
}
