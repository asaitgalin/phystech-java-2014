package ru.phystech.java2.asaitgalin.db.table.impl;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTable;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRowProvider;
import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;
import ru.phystech.java2.asaitgalin.db.table.impl.xml.XMLReader;
import ru.phystech.java2.asaitgalin.db.table.impl.xml.XMLWriter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseTableRowProviderImpl implements DatabaseTableRowProvider {

    @Override
    public DatabaseTableRow createFor(DatabaseTable table) {
        List<Class<?>> columnTypes = new ArrayList<>();
        for (int i = 0; i < table.getColumnsCount(); ++i) {
            columnTypes.add(table.getColumnType(i));
        }
        return new DatabaseTableRowImpl(columnTypes);
    }

    @Override
    public DatabaseTableRow createFor(DatabaseTable table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException {
        List<Class<?>> columnTypes = new ArrayList<>();
        for (int i = 0; i < table.getColumnsCount(); ++i) {
            columnTypes.add(table.getColumnType(i));
        }
        if (values.size() != columnTypes.size()) {
            throw new ColumnFormatException("values have another columns");
        }
        DatabaseTableRow row = new DatabaseTableRowImpl(columnTypes);
        for (int i = 0; i < values.size(); ++i) {
            row.setColumnAt(i, values.get(i));
        }
        return row;
    }

    @Override
    public DatabaseTableRow deserialize(DatabaseTable table, String value) throws ParseException {
        if (table == null || value == null) {
            throw new IllegalArgumentException("table or value is null");
        }
        DatabaseTableRow row = createFor(table);
        XMLReader reader = new XMLReader(value);
        for (int i = 0; i < table.getColumnsCount(); ++i) {
            row.setColumnAt(i, reader.readValue(table.getColumnType(i)));
        }
        reader.close();
        return row;
    }

    @Override
    public String serialize(DatabaseTable table, DatabaseTableRow value) throws ColumnFormatException {
        if (table == null || value == null) {
            throw new IllegalArgumentException("table or value is null");
        }
        XMLWriter writer = new XMLWriter();
        try {
            for (int i = 0; i < table.getColumnsCount(); ++i) {
                writer.writeValue(value.getColumnAt(i));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("row does not match table", e);
        }
        writer.close();
        return writer.toString();
    }
}
