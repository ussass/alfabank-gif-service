package com.example.alfabank.util;

import com.example.alfabank.feignClient.ExchangeClient;
import com.example.alfabank.model.Exchange;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CurrencyRateHandler {

    private final String url;

    private final String id;

    private final String currency;

    private final ExchangeClient exchangeClient;

    public CurrencyRateHandler(String url, String id, String currency, ExchangeClient exchangeClient) {
        this.url = url;
        this.id = id;
        this.currency = currency;
        this.exchangeClient = exchangeClient;
    }

    public boolean higherThanYesterday() {

        String currentRate = url + "latest.json?app_id=" + id;

        URI uri = URI.create(currentRate);

        Exchange currentExchange = exchangeClient.getExchange(uri);

        BigDecimal currencyRate = currentExchange.getRates().get(currency);

        LocalDateTime yesterdayDate = LocalDateTime.now().minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        String yesterday = format.format(yesterdayDate);

        String yesterdayRate = url + "historical/" + yesterday + ".json?app_id=" + id;
        uri = URI.create(yesterdayRate);

        Exchange yesterdayExchange = exchangeClient.getExchange(uri);

        BigDecimal yesterdayCurrencyRate = yesterdayExchange.getRates().get(currency);

        return currencyRate.compareTo(yesterdayCurrencyRate) >= 0;
    }
}
