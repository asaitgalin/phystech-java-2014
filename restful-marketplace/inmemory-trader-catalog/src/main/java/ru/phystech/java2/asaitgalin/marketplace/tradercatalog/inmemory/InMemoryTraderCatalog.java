package ru.phystech.java2.asaitgalin.marketplace.tradercatalog.inmemory;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.marketplace.api.TraderCatalog;
import ru.phystech.java2.asaitgalin.marketplace.model.Trader;
import ru.phystech.java2.asaitgalin.marketplace.model.TraderDescription;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTraderCatalog implements TraderCatalog {
    private Map<String, Trader> traderMap = new ConcurrentHashMap<String, Trader>();

    @Override
    public Trader getTraderById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id argument is invalid");
        }
        return traderMap.get(id);
    }

    @Override
    public void putTrader(Trader trader) {
        if (trader == null) {
            throw new IllegalArgumentException("trader argument is null");
        }
        traderMap.put(trader.getId(), trader);
    }

    @Override
    public boolean removeTrader(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id argument is null");
        }
        return traderMap.remove(id) != null;
    }

    @Override
    public Trader createTrader(TraderDescription description) {
        if (description == null) {
            throw new IllegalArgumentException("description argument is null");
        }
        if (!Arrays.asList(Locale.getISOCountries()).contains(description.getCountryCode())) {
            throw new IllegalArgumentException("description has invalid country code");
        }
        Trader trader = new Trader();
        trader.setName(description.getName());
        trader.setCountryCode(description.getCountryCode());
        trader.setId(UUID.randomUUID().toString());
        putTrader(trader);
        return trader;
    }

    @Override
    public List<String> getTradersIds() {
        return new ArrayList<String>(traderMap.keySet());
    }
}
