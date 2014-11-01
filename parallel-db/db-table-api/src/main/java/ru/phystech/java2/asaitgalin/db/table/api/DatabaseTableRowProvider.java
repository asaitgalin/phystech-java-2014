package ru.phystech.java2.asaitgalin.db.table.api;

import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;

import java.text.ParseException;
import java.util.List;

/**
 * Управляющий класс для работы с {@link DatabaseTableRow}
 *
 * Данный интерфейс не является потокобезопасным.
 */
public interface DatabaseTableRowProvider {
    /**
     * Создает новый пустой {@link DatabaseTableRow} для указанной таблицы.
     *
     * @param table Таблица, которой должен принадлежать {@link DatabaseTableRow}.
     * @return Пустой {@link DatabaseTableRow}, нацеленный на использование с этой таблицей.
     */
    DatabaseTableRow createFor(DatabaseTable table);

    /**
     * Создает новый {@link DatabaseTableRow} для указанной таблицы, подставляя туда переданные значения.
     *
     * @param table Таблица, которой должен принадлежать {@link DatabaseTableRow}.
     * @param values Список значений, которыми нужно проинициализировать поля Storeable.
     * @return {@link DatabaseTableRow}, проинициализированный переданными значениями.
     * @throws ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException При несоответствии типа переданного значения и колонки.
     * @throws IndexOutOfBoundsException При несоответствии числа переданных значений и числа колонок.
     */
    DatabaseTableRow createFor(DatabaseTable table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException;
    /**
     * Преобразовывает строку в объект {@link DatabaseTableRow}, соответствующий структуре таблицы.
     *
     * @param table Таблица, которой должен принадлежать {@link DatabaseTableRow}.
     * @param value Строка, из которой нужно прочитать {@link DatabaseTableRow}.
     * @return Прочитанный {@link DatabaseTableRow}.
     *
     * @throws java.text.ParseException - при каких-либо несоответстиях в прочитанных данных.
     */
    DatabaseTableRow deserialize(DatabaseTable table, String value) throws ParseException;

    /**
     * Преобразовывает объект {@link DatabaseTableRow} в строку.
     *
     * @param table Таблица, которой должен принадлежать {@link DatabaseTableRow}.
     * @param value {@link DatabaseTableRow}, который нужно записать.
     * @return Строка с записанным значением.
     *
     * @throws ColumnFormatException При несоответствии типа в {@link DatabaseTableRow} и типа колонки в таблице.
     */
    String serialize(DatabaseTable table, DatabaseTableRow value) throws ColumnFormatException;
}
