package com.coinbase.exchange.api.products;

import com.coinbase.exchange.api.exchange.CoinbaseExchange;
import com.coinbase.exchange.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * Created by robevansuk on 03/02/2017.
 */
public class ProductService {

    public static final String PRODUCTS_ENDPOINT = "/products";

    final CoinbaseExchange exchange;

    public ProductService(final CoinbaseExchange exchange) {
        this.exchange = exchange;
    }

    // no paged products necessary
    public List<Product> getProducts() {
        return exchange.getAsList(PRODUCTS_ENDPOINT, new ParameterizedTypeReference<Product[]>() {
        });
    }

    public Candles getCandles(String productId) {
        return new Candles(exchange.get(PRODUCTS_ENDPOINT + "/" + productId + "/candles", new ParameterizedTypeReference<List<String[]>>() {
        }));
    }

    public HashMap<String, Stat> getStats(String productId) {
        return exchange.get(PRODUCTS_ENDPOINT + "/" + productId + "/stats",
                new ParameterizedTypeReference<HashMap<String, Stat>>() {

        });
    }

//    public void statsHelper() {
//        JsonNode node = mapper.readTree(test);//, new ParameterizedTypeReference<HashMap<String, Stat>[]>(){});
//        System.out.println(node.getNodeType());
//
//        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
//            Map.Entry<String, JsonNode> entry = it.next();
//            String key = entry.getKey();
//            String txt = entry.getValue().toString();
//            System.out.println("subnode: " + txt);
//            if (txt.length() > 0) {
//                Stat nStat = mapper.readValue(txt, Stat.class);
//                System.out.println("    " + key + ":  Stat: " + nStat);
//            }
//        }
//
//    }

    public Candles getCandles(String productId, Map<String, String> queryParams) {
        StringBuffer url = new StringBuffer(PRODUCTS_ENDPOINT + "/" + productId + "/candles");
        if (queryParams != null && queryParams.size() != 0) {
            url.append("?");
            url.append(queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(joining("&")));
        }
        return new Candles(exchange.get(url.toString(), new ParameterizedTypeReference<List<String[]>>() {}));
    }

    /**
     * If either one of the start or end fields are not provided then both fields will be ignored.
     * If a custom time range is not declared then one ending now is selected.
     */
    public Candles getCandles(String productId, Instant startTime, Instant endTime, Granularity granularity) {

        Map<String, String> queryParams = new HashMap<>();

        if (startTime != null) {
            queryParams.put("start", startTime.toString());
        }
        if (endTime != null) {
            queryParams.put("end", endTime.toString());
        }
        if (granularity != null) {
            queryParams.put("granularity", granularity.get());
        }

        return getCandles(productId, queryParams);
    }

    /**
     * The granularity field must be one of the following values: {60, 300, 900, 3600, 21600, 86400}
     */
    public Candles getCandles(String productId, Granularity granularity) {
        return getCandles(productId, null, null, granularity);
    }

    /**
     *  If either one of the start or end fields are not provided then both fields will be ignored.
     */
    public Candles getCandles(String productId, Instant start, Instant end) {
        return getCandles(productId, start, end, null);
    }
}
