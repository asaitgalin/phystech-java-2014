package ru.phystech.java2.asaitgalin.marketplace.warestorage.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.phystech.java2.asaitgalin.marketplace.api.WareStorage;
import ru.phystech.java2.asaitgalin.marketplace.model.Ware;
import ru.phystech.java2.asaitgalin.marketplace.model.WareDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RestClientWaresStorage implements WareStorage {
    @Autowired
    private RestTemplate restTemplate;

    @Value("http://${ru.phystech.java2.asaitgalin.marketplace.host:localhost}:" +
            "${ru.phystech.java2.asaitgalin.marketplace.port:40000}/" +
            "${ru.phystech.java2.asaitgalin.marketplace.waresPrefix:}")
    private String baseUrl;

    @Override
    public Ware getWareById(String id) {
        return restTemplate.getForObject(buildWareUrl(id), Ware.class);
    }

    @Override
    public void putWare(Ware ware) {
        restTemplate.put(buildWareUrl(ware.getId()), ware);
    }

    @Override
    public boolean removeWare(String id) {
        restTemplate.delete(buildWareUrl(id));
        return true;
    }

    @Override
    public Ware createWare(WareDescription description) {
        Ware ware = new Ware();
        ware.setName(description.getName());
        ware.setMeasuring(description.getMeasuring());
        ware.setId(UUID.randomUUID().toString());
        putWare(ware);
        return ware;
    }

    @Override
    public List<String> getWaresIds() {
        return restTemplate.getForObject(buildWareUrl(""), StringList.class);
    }

    private String buildWareUrl(String id) {
        return baseUrl + "wares/" + id;
    }

    public static class StringList extends ArrayList<String> {}
}
