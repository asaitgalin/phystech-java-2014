package ru.phystech.java2.asaitgalin.db.shell.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableProvider;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.table.api.TableProviderFactory;

import java.io.IOException;

@Service
public class DatabaseState {
    @Value("${db.path}")
    private String databasePath;
    public DatabaseTable table;
    public DatabaseTableProvider tableProvider;

    @Autowired
    public DatabaseTableRowProvider tableRowProvider;

    @Autowired
    void setTableProviderFactory(TableProviderFactory<DatabaseTableProvider> factory) {
        try {
            tableProvider = factory.create(databasePath);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
