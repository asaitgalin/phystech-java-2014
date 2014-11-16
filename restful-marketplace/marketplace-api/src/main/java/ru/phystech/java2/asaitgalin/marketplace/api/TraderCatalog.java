package ru.phystech.java2.asaitgalin.marketplace.api;

import ru.phystech.java2.asaitgalin.marketplace.model.Trader;
import ru.phystech.java2.asaitgalin.marketplace.model.TraderDescription;

import java.util.List;

public interface TraderCatalog {
    /**
     * Retrieves {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} by id from storage
     * @param id
     * @return {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} or null if id not found
     * @throws java.lang.IllegalArgumentException if argument is invalid (null or empty)
     */
    Trader getTraderById(String id);

    /**
     * Puts {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} to storage
     * @param trader
     * @throws java.lang.IllegalArgumentException if argument is null
     */
    void putTrader(Trader trader);

    /**
     * Removes {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} using given id
     * @param id
     * @return true on success or false if id not found
     * @throws java.lang.IllegalArgumentException if argument is invalid(null or empty)
     */
    boolean removeTrader(String id);

    /**
     * Creates {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} from description with unique id
     * and puts it to catalog automatically
     * @param description
     * @return {@link ru.phystech.java2.asaitgalin.marketplace.model.Trader} instance
     * @throws java.lang.IllegalArgumentException if argument is null or has illegal fields
     */
    Trader createTrader(TraderDescription description);

    /**
     * @return List of traders unique ids
     */
    List<String> getTradersIds();
}
