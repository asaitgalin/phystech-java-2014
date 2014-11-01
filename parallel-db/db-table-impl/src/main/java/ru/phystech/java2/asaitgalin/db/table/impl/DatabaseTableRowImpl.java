package ru.phystech.java2.asaitgalin.db.table.impl;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow;
import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTableRowImpl implements DatabaseTableRow {
    private List<Class<?>> columnTypes = new ArrayList<>();
    private List<Object> columnData = new ArrayList<>();

    public DatabaseTableRowImpl(List<Class<?>> columnTypes) {
        this.columnTypes = columnTypes;
        for (int i = 0; i < columnTypes.size(); ++i) {
            columnData.add(null);
        }
    }

    @Override
    public void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        if (value != null) {
            checkClass(columnIndex, value.getClass());
        }
        columnData.set(columnIndex, value);
    }

    @Override
    public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
        checkIndex(columnIndex);
        return columnData.get(columnIndex);
    }

    @Override
    public Integer getIntAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Integer.class);
        return (Integer) columnData.get(columnIndex);
    }

    @Override
    public Long getLongAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Long.class);
        return (Long) columnData.get(columnIndex);
    }

    @Override
    public Byte getByteAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Byte.class);
        return (Byte) columnData.get(columnIndex);
    }

    @Override
    public Float getFloatAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Float.class);
        return (Float) columnData.get(columnIndex);
    }

    @Override
    public Double getDoubleAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Double.class);
        return (Double) columnData.get(columnIndex);
    }

    @Override
    public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, Boolean.class);
        return (Boolean) columnData.get(columnIndex);
    }

    @Override
    public String getStringAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkIndex(columnIndex);
        checkClass(columnIndex, String.class);
        return (String) columnData.get(columnIndex);
    }

    @Override
    public ImmutableList<Class<?>> getColumnTypes() {
        return new ImmutableList.Builder<Class<?>>()
                   .addAll(columnTypes)
                   .build();
    }

    @Override
    public String toString() {
        return String.format("[%s]", StringUtils.join(columnData, ","));
    }

    private void checkIndex(int index) throws IndexOutOfBoundsException {
        if (index >= columnTypes.size() || index < 0) {
            throw new IndexOutOfBoundsException(String.format("table row: list index %d out of bounds", index));
        }
    }

    private void checkClass(int index, Class<?> cl) throws ColumnFormatException {
        if (!cl.isAssignableFrom(columnTypes.get(index))) {
            throw new ColumnFormatException(String.format("table row: invalid type %s for column %d, expected type %s",
                    cl.getName(), index, columnTypes.get(index).getName()));
        }
    }
}
