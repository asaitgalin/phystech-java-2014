package ru.phystech.java2.asaitgalin.db.table.api.exceptions;

/**
 * Бросается при попытке извлечь из колонки {@link ru.phystech.java2.asaitgalin.db.table.api.DatabaseTableRow} значение не соответствующего типа,
 * либо подставить в колонку значение несоответствующего типа.
 */
public class ColumnFormatException extends IllegalArgumentException {

    public ColumnFormatException() {
    }

    public ColumnFormatException(String s) {
        super(s);
    }

    public ColumnFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ColumnFormatException(Throwable cause) {
        super(cause);
    }
}

