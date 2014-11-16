package ru.phystech.java2.asaitgalin.marketplace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.phystech.java2.asaitgalin.marketplace.api.TraderCatalog;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.NotFoundException;
import ru.phystech.java2.asaitgalin.marketplace.model.Trader;
import ru.phystech.java2.asaitgalin.marketplace.model.TraderDescription;
import ru.phystech.java2.asaitgalin.marketplace.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebMarketplaceController {
    public static final String INDEX_VIEW_NAME = "index";
    public static final String TRADERS_VIEW_NAME = "traders";

    public static final String TRADERS_LIST_ATTR = "tradersList";
    public static final String PAGE_ATTR = "page";
    public static final String TOTAL_COUNT_ATTR = "totalCount";
    public static final String PAGES_LIST_ATTR = "pagesList";

    public static final int PAGE_SIZE = 10;

    @Autowired
    private TraderCatalog traderCatalog;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String handleIndex() {
        return INDEX_VIEW_NAME;
    }

    @RequestMapping(value = "/traders", method = RequestMethod.GET)
    public ModelAndView listTraders(@RequestParam(value = PAGE_ATTR, defaultValue = "1") int page) {
        Page<Trader> tradersPage = loadTradersPage(page);
        ModelAndView result = new ModelAndView(TRADERS_VIEW_NAME);
        writePageIntoModel(tradersPage, result);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public String addTrader(@RequestParam("name") String name, @RequestParam("country-code") String countryCode) {
        TraderDescription description = new TraderDescription();
        description.setName(name);
        description.setCountryCode(countryCode);
        traderCatalog.createTrader(description);
        return "redirect:/traders";
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public String addTrader(@RequestParam("id") String id) {
        if (!traderCatalog.removeTrader(id)) {
            throw new NotFoundException("Incorrect trader id: " + id);
        }
        return "redirect:/traders";
    }

    private Page<Trader> loadTradersPage(int pageNumber) {
        if (pageNumber < 1) {
            throw new BadRequestException("Incorrect page number: " + pageNumber);
        }

        List<String> tradersIds = traderCatalog.getTradersIds();
        int totalCount = tradersIds.size();

        int offset = (pageNumber - 1) * PAGE_SIZE;

        offset = offset > totalCount ? totalCount : offset;
        int limit = PAGE_SIZE < totalCount - offset ? PAGE_SIZE : totalCount - offset;

        List<String> resultWareIds = tradersIds.subList(offset, offset + limit);
        List<Trader> tradersPageData = resultWareIds
                .stream()
                .map(traderCatalog::getTraderById)
                .collect(Collectors.toList());

        return new Page<>(tradersPageData, totalCount, pageNumber);
    }

    private List<String> getPagesList(int totalCount) {
        List<String> pages = new ArrayList<>();
        for (int i = 1; i <= (totalCount - 1) / PAGE_SIZE + 1; ++i) {
            pages.add(Integer.toString(i));
        }
        return pages;
    }

    private void writePageIntoModel(Page<Trader> traderPage, ModelAndView result) {
        result
                .addObject(TRADERS_LIST_ATTR, traderPage.getData())
                .addObject(PAGE_ATTR, traderPage.getPageNumber())
                .addObject(PAGES_LIST_ATTR, getPagesList(traderPage.getTotalCount()))
                .addObject(TOTAL_COUNT_ATTR, traderPage.getTotalCount());
    }
}
