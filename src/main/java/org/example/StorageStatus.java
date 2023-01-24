package org.example;

import java.util.concurrent.ThreadLocalRandom;

public enum StorageStatus {
    normal, good, perfect;

    private static final StorageStatus[] VALUES = values();

    public static StorageStatus getRandomStatus() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
