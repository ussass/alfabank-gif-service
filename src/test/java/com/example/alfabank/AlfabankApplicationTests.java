package com.example.alfabank;

import com.example.alfabank.feignClient.*;
import com.example.alfabank.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlfabankApplicationTests {

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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeClient exchangeClient;

    @MockBean
    private GifClient gifClient;

    @Test
    public void mockExchange() throws Exception {

        Exchange currentExchangeTest = new Exchange();
        Map<String, BigDecimal> currentRateMap = new HashMap<>();
        currentRateMap.put("RUB", new BigDecimal("72.1"));
        currentExchangeTest.setRates(currentRateMap);

        Exchange yesterdayExchangeTest = new Exchange();
        Map<String, BigDecimal> yesterdayRateMap = new HashMap<>();
        yesterdayRateMap.put("RUB", new BigDecimal("32.1"));
        yesterdayExchangeTest.setRates(yesterdayRateMap);

        Gif gif = new Gif();
        Map<String, Object> data = new HashMap<>();
        data.put("id", "OjuvY2MgK0sE");
        gif.setData(data);

        LocalDateTime date = LocalDateTime.now().minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        String yesterday = format.format(date);

        URI currentUri = URI.create(ratesUrl + "latest.json?app_id=" + id);
        URI yesterdayUri = URI.create(ratesUrl + "historical/" + yesterday + ".json?app_id=" + id);
        URI gifUri = URI.create(giphyUrl + "v1/gifs/random?api_key=" + key + "&tag=rich");

        Mockito.when(exchangeClient.getExchange(currentUri)).thenReturn(currentExchangeTest);
        Mockito.when(exchangeClient.getExchange(yesterdayUri)).thenReturn(yesterdayExchangeTest);
        Mockito.when(gifClient.getGif(gifUri)).thenReturn(gif);

        mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("<img src=\"https://i.giphy.com/media/OjuvY2MgK0sE/giphy.gif\">")));
    }
}
