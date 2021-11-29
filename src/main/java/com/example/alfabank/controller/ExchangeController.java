package com.example.alfabank.controller;

import com.example.alfabank.feignClient.*;
import com.example.alfabank.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExchangeController {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Value("${openExchangeRates.url}")
    private String ratesUrl;

    @Value("${openExchangeRates.id}")
    private String id;

    @Value("${currency}")
    private String currency;

    @Value("${giphy.url}")
    private String giphyUrl;

    @Value("${giphy.key}")
    private String key;

    private final ExchangeClient exchangeClient;

    private final GifClient gifClient;

    public ExchangeController(ExchangeClient exchangeClient, GifClient gifClient) {
        this.exchangeClient = exchangeClient;
        this.gifClient = gifClient;
    }

    @GetMapping
    public String exchange(Model model) {

        CurrencyRateHandler currencyRateHandler = new CurrencyRateHandler(ratesUrl, id, currency, exchangeClient);

        String gifTag = currencyRateHandler.higherThanYesterday() ? "rich" : "broken";
        logger.info("gifTag is determined by {} currency: {}", currency, gifTag);

        GifFinder gifFinder = new GifFinder(giphyUrl, key, gifTag, gifClient);

        String gifId = gifFinder.getRandomGifId();
        logger.info("id of random gif: {}", gifId);
        String gifUrl = "https://i.giphy.com/media/" + gifId + "/giphy.gif";
        model.addAttribute("title", gifTag);
        model.addAttribute("gifUrl", gifUrl);

        return "index";
    }
}
