package ru.phystech.java2.asaitgalin.table.api;

import java.io.IOException;

/**
 * Представляет интерфейс для создание экземпляров {@link T}.
 *
 * Предполагается, что реализация интерфейса фабрики будет иметь публичный конструктор без параметров.
 */
public interface TableProviderFactory<T> {

    /**
     * Возвращает объект для работы с базой данных.
     *
     * @param path Директория с файлами базы данных.
     * @return Объект для работы с базой данных, который будет работать в указанной директории.
     *
     * @throws IllegalArgumentException Если значение директории null или имеет недопустимое значение.
     * @throws java.io.IOException В случае ошибок ввода/вывода.
     */
    T create(String path) throws IOException;
}
