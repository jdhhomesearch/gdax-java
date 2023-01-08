package com.coinbase.exchange.api.marketdata;

import com.coinbase.exchange.api.exchange.CoinbaseExchange;
import com.coinbase.exchange.model.Stat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;

import java.io.File;
import java.util.*;

/**
 * Created by robevansuk on 07/02/2017.
 */
public class MarketDataService {

    final CoinbaseExchange exchange;

    public MarketDataService(final CoinbaseExchange exchange) {
        this.exchange = exchange;
    }

    public static final String PRODUCT_ENDPOINT = "/products";

    public MarketData getMarketDataOrderBook(String productId, int level) {
        String marketDataEndpoint = PRODUCT_ENDPOINT + "/" + productId + "/book";
        if(level != 1)
            marketDataEndpoint += "?level=" + level;
       return exchange.get(marketDataEndpoint, new ParameterizedTypeReference<MarketData>(){});
    }

    public List<Trade> getTrades(String productId) {
        String tradesEndpoint = PRODUCT_ENDPOINT + "/" + productId + "/trades";
        return exchange.getAsList(tradesEndpoint, new ParameterizedTypeReference<Trade[]>(){});
    }

    public HashMap<String, Stat> getStats() {
        String statsEndpoint = PRODUCT_ENDPOINT + "/stats";
        return exchange.get(statsEndpoint,
                new ParameterizedTypeReference<HashMap<String, Stat>>() {
        });
    }
//    public Map<String, Stat> getStats2() {
//        String statsEndpoint = PRODUCT_ENDPOINT + "/stats";
//        String json = exchange.get(statsEndpoint, new ParameterizedTypeReference<String>(){});
//
//        System.out.println("---------------");
//        System.out.println(json);
//        System.out.println("---------------");
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode node = null;//, new ParameterizedTypeReference<HashMap<String, Stat>[]>(){});
//        Map<String, Stat> statMap = new HashMap<>();
//        try {
//            node = mapper.readTree(json);
//            System.out.println(node.getNodeType());
//
//            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
//                Map.Entry<String, JsonNode> entry = it.next();
//                String key = entry.getKey();
//                String txt = entry.getValue().toString();
//                System.out.println("subnode: " + txt);
//                if (txt.length() > 0) {
//                    Stat nStat = mapper.readValue(txt, Stat.class);
//                    System.out.println("    " + key + ":  Stat: " + nStat);
//                    statMap.put(key, nStat);
//                }
//            }
//            System.out.println(node);
//        } catch (JsonProcessingException e) {
//            System.err.println("exception while getting stats " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return statMap;
//    }
}
