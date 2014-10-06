package ru.phystech.java2.asaitgalin.db.table.impl;

import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.db.table.impl.fileio.StringEntryReader;
import ru.phystech.java2.asaitgalin.db.table.impl.fileio.StringEntryWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseTableSerializer {
    private static final int DIR_COUNT = 16;
    private static final int FILES_PER_DIR = 16;

    private File tableDir;
    private DatabaseTable table;
    private DatabaseTableRowProvider rowProvider;

    public DatabaseTableSerializer(File tableDir, DatabaseTable table,
                                   DatabaseTableRowProvider rowProvider) {
        this.tableDir = tableDir;
        this.table = table;
        this.rowProvider = rowProvider;
    }

    public void store(Map<String, DatabaseTableRow> tableData) throws IOException {
        for (int i = 0; i < DIR_COUNT; ++i) {
            for (int j = 0; j < FILES_PER_DIR; ++j) {
                Map<String, String> values = new HashMap<>();
                for (String s : tableData.keySet()) {
                    if (getKeyDir(s) == i && getKeyFile(s) == j) {
                        try {
                            values.put(s, rowProvider.serialize(table, table.get(s)));
                        } catch (Exception e) {
                            throw new IOException(e);
                        }
                    }
                }
                if (values.size() > 0) {
                    File keyDir = new File(tableDir, i + ".dir");
                    if (!keyDir.exists()) {
                        keyDir.mkdir();
                    }
                    File fileName = new File(keyDir, j + ".dat");
                    StringEntryWriter writer = new StringEntryWriter(fileName);
                    writer.writeEntries(values);
                }
            }
        }
    }

    public void load(Map<String, DatabaseTableRow> outputTable) throws IOException {
        for (File subDir : tableDir.listFiles()) {
            if (subDir.isDirectory()) {
                boolean hasFiles = false;
                for (File f : subDir.listFiles()) {
                    hasFiles = true;
                    StringEntryReader reader = new StringEntryReader(f);
                    while (reader.hasNextEntry()) {
                        Map.Entry<String, String> entry = reader.readNextEntry();
                        File validFile = new File(new File(tableDir, getKeyDir(entry.getKey()) + ".dir"),
                                getKeyFile(entry.getKey()) + ".dat");
                        if (!f.equals(validFile)) {
                            throw new IOException("Corrupted database");
                        }
                        try {
                            outputTable.put(entry.getKey(), rowProvider.deserialize(table, entry.getValue()));
                        } catch (Exception e) {
                            throw new IOException(e);
                        }
                    }
                }
                if (!hasFiles) {
                    throw new IOException("empty dir");
                }
            }
        }
    }

    private static int getKeyDir(String key) {
        return Math.abs(key.hashCode()) % DIR_COUNT;
    }

    private static int getKeyFile(String key) {
        return Math.abs(key.hashCode()) / DIR_COUNT % FILES_PER_DIR;
    }
}
