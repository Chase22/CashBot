package org.chase.telegram.cashbot;

public class VerificationException extends Exception {
    public VerificationException() {
    }

    public VerificationException(final String message) {
        super(message);
    }
}
