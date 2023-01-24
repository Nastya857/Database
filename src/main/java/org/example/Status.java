package org.example;

import java.util.concurrent.ThreadLocalRandom;

public enum Status {
    process, done, expects;

    private static final Status[] VALUES = values();

    public static Status getRandomStatus() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
