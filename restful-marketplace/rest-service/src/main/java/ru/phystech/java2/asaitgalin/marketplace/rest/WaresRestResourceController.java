package ru.phystech.java2.asaitgalin.marketplace.rest;

import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.phystech.java2.asaitgalin.marketplace.api.WareStorage;
import ru.phystech.java2.asaitgalin.marketplace.model.Ware;
import ru.phystech.java2.asaitgalin.marketplace.model.WareDescription;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.BadRequestException;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/wares")
public class WaresRestResourceController {
    @Autowired
    private WareStorage storage;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Ware getWare(@PathVariable String id) {
        Ware ware = storage.getWareById(id);
        if (ware == null) {
            throw new NotFoundException();
        }
        return ware;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createWare(@RequestBody WareDescription description) {
        Ware ware = storage.createWare(description);
        return ware.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void putWare(@PathVariable String id, @RequestBody Ware ware) {
        if (!id.equals(ware.getId())) {
            throw new BadRequestException();
        }
        storage.putWare(ware);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteWare(@PathVariable String id) {
        if (!storage.removeWare(id)) {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<String> listWares() {
        return storage.getWaresIds();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody String handleBadRequest(HttpServletRequest request, BadRequestException ex) {
        return "Bad request";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody String handleNotFound(HttpServletRequest req, NotFoundException ex) {
        return "404 Not Found";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody String handleException(HttpServletRequest req, Exception ex) {
        return "Exception " + ex + ":\n" + Throwables.getStackTraceAsString(ex);
    }

}
