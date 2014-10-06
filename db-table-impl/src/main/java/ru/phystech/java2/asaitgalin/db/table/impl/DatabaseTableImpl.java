package ru.phystech.java2.asaitgalin.db.table.impl;

import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseTableImpl implements DatabaseTable {

    private class TableTransaction {
        private Map<String, DatabaseTableRow> currentTable;

        public TableTransaction() {
            this.currentTable = new HashMap<>();
        }

        public void putRow(String key, DatabaseTableRow row) {
            currentTable.put(key, row);
        }

        public DatabaseTableRow getRow(String key) {
            return currentTable.get(key);
        }

        public void removeRow(String key) {
            currentTable.put(key, null);
        }

        public int getSize() {
            int count = 0;
            for (String key : currentTable.keySet()) {
                DatabaseTableRow newValue = currentTable.get(key);
                DatabaseTableRow oldValue = originalTable.get(key);
                if (newValue == null && oldValue != null) {
                    --count;
                } else if (newValue != null && oldValue == null) {
                    ++count;
                }
            }
            return originalTable.size() + count;
        }

        public int getChangesCount() {
            int count = 0;
            for (String key : currentTable.keySet()) {
                DatabaseTableRow newValue = currentTable.get(key);
                if (rowsHaveChanges(originalTable.get(key), newValue)) {
                    ++count;
                }
            }
            return count;
        }

        public int commitChanges() {
            int count = 0;
            for (String key : currentTable.keySet()) {
                DatabaseTableRow row = currentTable.get(key);
                if (rowsHaveChanges(originalTable.get(key), row)) {
                    if (row == null) {
                        originalTable.remove(key);
                    } else {
                        originalTable.put(key, row);
                    }
                    ++count;
                }
            }
            return count;
        }

        public void clear() {
            currentTable.clear();
        }

        private boolean rowsHaveChanges(DatabaseTableRow oldValue, DatabaseTableRow newValue) {
            if (newValue == null && oldValue == null) {
                return false;
            }
            if (newValue == null || oldValue == null) {
                return true;
            }
            return !oldValue.equals(newValue);
        }
    }

    private Map<String, DatabaseTableRow> originalTable;
    private String name;
    private List<Class<?>> columnTypes;
    private DatabaseTableSerializer serializer;

    protected final Lock transactionsLock = new ReentrantLock(true);

    private ThreadLocal<TableTransaction> localTransaction = new ThreadLocal<TableTransaction>() {
        @Override
        protected TableTransaction initialValue() {
            return new TableTransaction();
        }
    };

    DatabaseTableImpl(File tableDir, String name, DatabaseTableRowProvider rowProvider) {
        DatabaseTableSignatureWorker worker = new DatabaseTableSignatureWorker(tableDir);
        this.columnTypes = worker.readColumnTypes();
        this.originalTable = new HashMap<>();
        this.serializer = new DatabaseTableSerializer(tableDir, this, rowProvider);
        this.name = name;
        try {
            this.serializer.load(originalTable);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    DatabaseTableImpl(File tableDir, String name, DatabaseTableRowProvider rowProvider,
                      List<Class<?>> columnTypes) {
        this.columnTypes = columnTypes;
        this.originalTable = new HashMap<>();
        this.serializer = new DatabaseTableSerializer(tableDir, this, rowProvider);
        this.name = name;
        DatabaseTableSignatureWorker worker = new DatabaseTableSignatureWorker(tableDir);
        worker.writeColumnTypes(columnTypes);
    }

    @Override
    public int getColumnsCount() {
        return columnTypes.size();
    }

    @Override
    public Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException {
        if (columnIndex < 0 || columnIndex >= columnTypes.size()) {
            throw new IndexOutOfBoundsException(String.format("index %d out of bounds", columnIndex));
        }
        return columnTypes.get(columnIndex);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DatabaseTableRow get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        DatabaseTableRow value = localTransaction.get().getRow(key);
        return value == null ? originalTable.get(key) : value;
    }

    @Override
    public DatabaseTableRow put(String key, DatabaseTableRow value) throws ColumnFormatException {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }
        if (key.matches("\\s*") || key.split("\\s+").length != 1) {
            throw new IllegalArgumentException("key or value is empty");
        }
        checkValue(value);
        DatabaseTableRow oldValue = get(key);
        localTransaction.get().putRow(key, value);
        return oldValue;
    }

    @Override
    public DatabaseTableRow remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        DatabaseTableRow oldValue = get(key);
        if (oldValue == null) {
            return null;
        }
        localTransaction.get().removeRow(key);
        return oldValue;
    }

    @Override
    public int size() {
        return localTransaction.get().getSize();
    }

    @Override
    public int commit() throws IOException {
        try {
            transactionsLock.lock();
            int changesCount = localTransaction.get().commitChanges();
            localTransaction.get().clear();
            serializer.store(originalTable);
            return changesCount;
        } finally {
            transactionsLock.unlock();
        }
    }

    @Override
    public int rollback() {
        int count = localTransaction.get().getChangesCount();
        localTransaction.get().clear();
        return count;
    }

    @Override
    public int getChangesCount() {
        return localTransaction.get().getChangesCount();
    }

    private void checkValue(DatabaseTableRow value) {
        if (!columnTypes.equals(value.getColumnTypes())) {
            throw new IllegalArgumentException("alien database table row");
        }
    }

}
