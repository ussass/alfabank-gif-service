package com.example.alfabank.util;

import com.example.alfabank.feignClient.GifClient;

import java.net.URI;

public class GifFinder {

    private final String url;

    private final String key;

    private final String gifTag;

    private final GifClient gifClient;

    public GifFinder(String url, String key, String gifTag, GifClient gifClient) {
        this.url = url;
        this.key = key;
        this.gifTag = gifTag;
        this.gifClient = gifClient;
    }

    public String getRandomGifId() {

        String gifRequest = url + "v1/gifs/random?api_key=" + key + "&tag=" + gifTag;
        URI uri = URI.create(gifRequest);

        return (String) gifClient.getGif(uri).getData().get("id");
    }
}
