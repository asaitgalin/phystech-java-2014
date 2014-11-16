package ru.phystech.java2.asaitgalin.marketplace.tradercatalog.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.phystech.java2.asaitgalin.marketplace.api.TraderCatalog;
import ru.phystech.java2.asaitgalin.marketplace.model.Trader;
import ru.phystech.java2.asaitgalin.marketplace.model.TraderDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RestClientTraderCatalog implements TraderCatalog {
    @Autowired
    private RestTemplate restTemplate;

    @Value("http://${ru.phystech.java2.asaitgalin.marketplace.host:localhost}:" +
            "${ru.phystech.java2.asaitgalin.marketplace.port:40000}/" +
            "${ru.phystech.java2.asaitgalin.marketplace.tradersPrefix:}")
    private String baseUrl;

    @Override
    public Trader getTraderById(String id) {
        return restTemplate.getForObject(buildTraderUrl(id), Trader.class);
    }

    @Override
    public void putTrader(Trader trader) {
        restTemplate.put(buildTraderUrl(trader.getId()), trader);
    }

    @Override
    public boolean removeTrader(String id) {
        restTemplate.delete(buildTraderUrl(id));
        return true;
    }

    @Override
    public Trader createTrader(TraderDescription description) {
        Trader trader = new Trader();
        trader.setId(UUID.randomUUID().toString());
        trader.setName(description.getName());
        trader.setCountryCode(description.getCountryCode());
        putTrader(trader);
        return trader;
    }

    @Override
    public List<String> getTradersIds() {
        return restTemplate.getForObject(buildTraderUrl(""), StringList.class);
    }

    private String buildTraderUrl(String id) {
        return baseUrl + "traders/" + id;
    }

    public static class StringList extends ArrayList<String> {}
}
