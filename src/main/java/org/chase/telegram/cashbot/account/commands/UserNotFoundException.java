package org.chase.telegram.cashbot.account.commands;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(final String format, final Object... args) {
        super(String.format(format, args));
    }
}
