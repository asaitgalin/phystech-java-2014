package ru.phystech.java2.asaitgalin.db.table.impl;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.table.api.TableProviderFactory;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;

import java.io.File;
import java.io.IOException;

@Service
public class TableProviderFactoryImpl implements TableProviderFactory<DatabaseTableProvider> {
    @Override
    public DatabaseTableProvider create(String path) throws IOException {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("factory, create: directory name is invalid");
        }
        File dbDir = new File(path);
        if (!dbDir.exists()) {
            if (!dbDir.mkdir()) {
                throw new IOException("table provider unavailable");
            }
        } else {
            if (!dbDir.isDirectory()) {
                throw new IllegalArgumentException("provided name is not directory");
            }
        }
        return new DatabaseTableProviderImpl(dbDir);
    }
}
