package ru.phystech.java2.asaitgalin.marketplace.api;

import ru.phystech.java2.asaitgalin.marketplace.model.Ware;
import ru.phystech.java2.asaitgalin.marketplace.model.WareDescription;

import java.util.List;

public interface WareStorage {
    /**
     * Retrieves {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} by id from storage
     * @param id
     * @return {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} or null if id not found
     * @throws java.lang.IllegalArgumentException if argument is invalid (null or empty)
     */
    Ware getWareById(String id);

    /**
     * Adds {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} to storage
     * @param ware
     * @throws java.lang.IllegalArgumentException if argument is null
     */
    void addWare(Ware ware);

    /**
     * Removes {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} using given id
     * @param id
     * @return true on success or false if id not found
     * @throws java.lang.IllegalArgumentException if argument is invalid (null or empty)
     */
    boolean removeWare(String id);

    /**
     * Creates {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} from description with unique id
     * @param description
     * @return {@link ru.phystech.java2.asaitgalin.marketplace.model.Ware} instance
     * @throws java.lang.IllegalArgumentException if argument is null
     */
    Ware createWare(WareDescription description);

    /**
     * @return List of wares unique ids
     */
    List<String> getWaresIds();
}
