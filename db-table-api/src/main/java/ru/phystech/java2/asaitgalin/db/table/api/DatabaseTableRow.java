package ru.phystech.java2.asaitgalin.db.table.api;

import com.google.common.collect.ImmutableList;
import ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException;

/**
 * Список фиксированной структуры, строка таблицы {@link DatabaseTable}.
 *
 * Нумерация колонок с нуля. Позиция в списке соответствует колонке таблицы под тем же номером.
 *
 * Для получения объекта из нужной колонки воспользуйтесь соответствующим геттером.
 * Для установки объекта а колонку воспользуйтесь {@link #setColumnAt(int, Object)} .
 */
public interface DatabaseTableRow {

    /**
     * Установить значение в колонку
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @param value - значение, которое нужно установить.
     *              Может быть null.
     *              Тип значения должен соответствовать декларированному типу колонки.
     * @throws ru.phystech.java2.asaitgalin.db.table.api.exceptions.ColumnFormatException - Тип значения не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, не приводя его к конкретному типу.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, без приведения типа. Может быть null.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Integer.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Integer. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Integer getIntAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Long.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Long. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Long getLongAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Byte.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Byte. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Byte getByteAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Float.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Float. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Float getFloatAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Double.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Double. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Double getDoubleAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к Boolean.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к Boolean. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    Boolean getBooleanAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает значение из данной колонки, приведя его к String.
     * @param columnIndex - индекс колонки в таблице, начиная с нуля
     * @return - значение в этой колонке, приведенное к String. Может быть null.
     * @throws ColumnFormatException - Запрошенный тип не соответствует типу колонки.
     * @throws IndexOutOfBoundsException - Неверный индекс колонки.
     */
    String getStringAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException;

    /**
     * Возвращает список типов колонок строки таблицы.
     * Используется для проверки соответствия строки таблице с ее типом колонок
     * @return - immutable список типов
     */
    ImmutableList<Class<?>> getColumnTypes();
}
