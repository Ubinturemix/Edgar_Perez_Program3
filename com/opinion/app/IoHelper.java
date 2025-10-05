package com.opinion.app;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class IoHelper {
    private IoHelper() {}

    public static <T> List<T> readAll(Path file) throws IOException {
        if (Files.notExists(file)) return List.of();
        try (var in = new ObjectInputStream(Files.newInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<T> list = (List<T>) in.readObject();
            return list;
        } catch (EOFException e) {
            return List.of();
        } catch (ClassNotFoundException e) {
            throw new IOException("Deserialization failure", e);
        }
    }

    public static <T> void writeAll(Path file, List<T> list) throws IOException {
        Files.createDirectories(file.getParent());
        Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
        try (var out = new ObjectOutputStream(Files.newOutputStream(tmp))) {
            out.writeObject(new ArrayList<>(list));
            out.flush();
        }
        Files.move(tmp, file,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }
}
