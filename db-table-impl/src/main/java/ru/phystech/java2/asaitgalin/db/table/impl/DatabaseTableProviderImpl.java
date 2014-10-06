package ru.phystech.java2.asaitgalin.db.table.impl;

import org.apache.commons.io.FileUtils;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseTableProviderImpl implements DatabaseTableProvider {
    private static final String TABLE_NAME_FORMAT = "[A-Za-zА-Яа-я0-9]+";

    private final ReadWriteLock tableProviderTransactionLock = new ReentrantReadWriteLock(true);
    private File dbDirectory;
    private Map<String, DatabaseTableImpl> tableMap = new HashMap<>();
    private DatabaseTableRowProviderImpl rowProvider;

    public DatabaseTableProviderImpl(File dbDirectory) {
        if (dbDirectory == null) {
            throw new IllegalArgumentException("bad db directory name");
        }
        if (!dbDirectory.isDirectory()) {
            throw new IllegalArgumentException("name is not a directory");
        }
        this.rowProvider = new DatabaseTableRowProviderImpl();
        for (File f : dbDirectory.listFiles()) {
            if (f.isFile()) {
                continue;
            }
            DatabaseTableImpl table = new DatabaseTableImpl(f, f.getName(), rowProvider);
            tableMap.put(f.getName(), table);
        }
        this.dbDirectory = dbDirectory;
    }

    @Override
    public DatabaseTable createTable(String name, List<Class<?>> columnTypes) throws IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("invalid name");
        }
        if (!name.matches(TABLE_NAME_FORMAT)) {
            throw new RuntimeException("incorrect table name");
        }
        if (columnTypes == null || columnTypes.isEmpty()) {
            throw new IllegalArgumentException("columnTypes are null or empty");
        }
        for (Class<?> cl : columnTypes) {
            if (cl == null || DatabaseTableTypes.getNameByClass(cl) == null) {
                throw new IllegalArgumentException("invalid column type");
            }
        }
        try {
            tableProviderTransactionLock.writeLock().lock();
            File tableDir = new File(dbDirectory, name);
            if (tableDir.exists()) {
                return null;
            }
            if (!tableDir.mkdir()) {
                throw new IOException("failed to create table directory");
            }
            DatabaseTableImpl table = new DatabaseTableImpl(tableDir, name, rowProvider, columnTypes);
            tableMap.put(name, table);
            return table;
        } finally {
            tableProviderTransactionLock.writeLock().unlock();
        }
    }

    @Override
    public DatabaseTable getTable(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("bad table name");
        }
        if (!name.matches(TABLE_NAME_FORMAT)) {
            throw new IllegalArgumentException("table name contains bad symbols");
        }
        try {
            tableProviderTransactionLock.readLock().lock();
            return tableMap.get(name);
        } finally {
            tableProviderTransactionLock.readLock().unlock();
        }
    }

    @Override
    public void removeTable(String name) throws IOException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("bad table name");
        }
        try {
            tableProviderTransactionLock.writeLock().lock();
            File tableDir = new File(dbDirectory, name);
            if (!tableDir.exists()) {
                throw new IllegalStateException("table does not exist");
            }
            tableMap.remove(name);
            FileUtils.deleteDirectory(tableDir);
        } finally {
            tableProviderTransactionLock.writeLock().unlock();
        }
    }

}
