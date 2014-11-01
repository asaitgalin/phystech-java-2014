package ru.phystech.java2.asaitgalin.db.table.api;

import ru.phystech.java2.asaitgalin.table.api.Table;

/**
 * Расширение Table. Представляет из себя таблицу с колонками.
 * Строкой таблицы является {@link DatabaseTableRow}
 */
public interface DatabaseTable extends Table<DatabaseTableRow> {

    /**
     * Возвращает количество колонок в таблице.
     *
     * @return Количество колонок в таблице.
     */
    int getColumnsCount();

    /**
     * Возвращает тип значений в колонке.
     *
     * @param columnIndex Индекс колонки. Начинается с нуля.
     * @return Класс, представляющий тип значения.
     *
     * @throws IndexOutOfBoundsException - неверный индекс колонки
     */
    Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException;

    /**
     * Возвращает количество сделанных изменений
     * @return int
     */
    int getChangesCount();
}
