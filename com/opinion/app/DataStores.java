package com.opinion.app;

public final class DataStores {
    private DataStores() {}
    public static DataStore defaultStore() {
        String impl = System.getProperty("opinion.ds", "file");
        return switch (impl) {
            case "mem" -> DataStoreMemory.getInstance();
            default    -> DataStoreFile.getInstance();
        };
    }
}
