package ru.phystech.java2.asaitgalin.marketplace.warestorage.inmemory;

import org.springframework.stereotype.Service;
import ru.phystech.java2.asaitgalin.marketplace.api.WareStorage;
import ru.phystech.java2.asaitgalin.marketplace.model.Ware;
import ru.phystech.java2.asaitgalin.marketplace.model.WareDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Service
public class InMemoryWareStorage implements WareStorage {
    private Map<String, Ware> wareMap = new ConcurrentHashMap<String, Ware>();


    @Override
    public Ware getWareById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id argument is invalid");
        }
        return wareMap.get(id);
    }

    @Override
    public void putWare(Ware ware) {
        if (ware == null) {
            throw new IllegalArgumentException("ware argument is null");
        }
        wareMap.put(ware.getId(), ware);
    }

    @Override
    public boolean removeWare(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return wareMap.remove(id) != null;
    }

    @Override
    public Ware createWare(WareDescription description) {
        if (description == null) {
            throw new IllegalArgumentException("description argument is null");
        }
        Ware ware = new Ware();
        ware.setName(description.getName());
        ware.setMeasuring(description.getMeasuring());
        ware.setId(UUID.randomUUID().toString());
        putWare(ware);
        return ware;
    }

    @Override
    public List<String> getWaresIds() {
        return new ArrayList<String>(wareMap.keySet());
    }
}
