package ru.phystech.java2.asaitgalin.marketplace.rest;

import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.phystech.java2.asaitgalin.marketplace.api.TraderCatalog;
import ru.phystech.java2.asaitgalin.marketplace.model.Trader;
import ru.phystech.java2.asaitgalin.marketplace.model.TraderDescription;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.BadRequestException;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/traders")
public class TradersRestResourceController {
    @Autowired
    private TraderCatalog traderCatalog;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Trader getTrader(@PathVariable String id) {
       Trader trader = traderCatalog.getTraderById(id);
        if (trader == null) {
            throw new NotFoundException();
        }
        return trader;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createTrader(@RequestBody TraderDescription description) {
        Trader trader = traderCatalog.createTrader(description);
        return trader.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void putTrader(@PathVariable String id, @RequestBody Trader trader) {
        if (!id.equals(trader.getId())) {
            throw new BadRequestException();
        }
        traderCatalog.putTrader(trader);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removeTrader(@PathVariable String id) {
        if (!traderCatalog.removeTrader(id)) {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<String> listTraders() {
        return traderCatalog.getTradersIds();
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
