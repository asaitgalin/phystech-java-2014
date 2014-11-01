package ru.phystech.java2.asaitgalin.db.table.api;

import ru.phystech.java2.asaitgalin.table.api.TableProvider;

import java.io.IOException;
import java.util.List;

public interface DatabaseTableProvider extends TableProvider {

    /**
     * Создаёт таблицу с указанным названием.
     * Создает новую таблицу. Совершает необходимые дисковые операции.
     *
     * @param name Название таблицы.
     * @param columnTypes Типы колонок таблицы. Не может быть пустой.
     * @return Объект, представляющий таблицу. Если таблица с указанным именем существует, возвращает null.
     *
     * @throws IllegalArgumentException Если название таблицы null или имеет недопустимое значение. Если список типов
     *                                  колонок null или содержит недопустимые значения.
     * @throws java.io.IOException При ошибках ввода/вывода.
     */
    DatabaseTable createTable(String name, List<Class<?>> columnTypes) throws IOException;

    /**
     * Расширение getTable у интерфейса {@link ru.phystech.java2.asaitgalin.table.api.TableProvider}
     * Возвращает таблицу с указанным названием.
     *
     * @param name Название таблицы.
     * @return Объект, представляющий таблицу. Если таблицы с указанным именем не существует, возвращает null.
     *
     * @throws IllegalArgumentException Если название таблицы null или имеет недопустимое значение.
     */
    @Override
    DatabaseTable getTable(String name);
}
