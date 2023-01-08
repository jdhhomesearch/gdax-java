package com.coinbase.exchange.api.marketdata;

import com.coinbase.exchange.api.exchange.CoinbaseExchangeImpl;
import com.coinbase.exchange.model.Stat;
import com.coinbase.exchange.model.Stat24Hour;
import com.coinbase.exchange.model.Stat30Day;
import com.coinbase.exchange.security.Signature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ren7881 on 20/03/2017.
 */
public class OrderItemDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * This is now out of date - the api delivers a list of strings - amount, price, order Id. NOT number of orders as this test shows.
     * @throws IOException
     */
    @Test
    public void testDesirialization() throws IOException {
        String test = "{\n" +
                "    \"sequence\": \"3\",\n" +
                "    \"bids\": [\n" +
                "        [ \"111.96\", \"2.11111\", 3 ],\n" +
                "        [ \"295.96\", \"4.39088265\", 2 ]\n" +
                "    ],\n" +
                "    \"asks\": [\n" +
                "        [ \"555.97\", \"66.5656565\", 10 ],\n" +
                "        [ \"295.97\", \"25.23542881\", 12 ]\n" +
                "    ]\n" +
                "}";

        MarketData marketData = mapper.readValue(test, MarketData.class);
        assertEquals(marketData.getAsks().size(), 2);
        assertEquals(marketData.getBids().size(), 2);
        assertEquals(marketData.getSequence(), 3L);
    }

    /**
     * This is now out of date - the api delivers a list of strings - amount, price, order Id. NOT number of orders as this test shows.
     * @throws IOException
     */
    @Test
    public void testDesirializationOfStats() throws IOException {
        String test = "{\n" +
                "   \"SNX-BTC\":{\n" +
                "      \"stats_30day\":{\n" +
                "         \"volume\":\"1495740.57\"\n" +
                "      },\n" +
                "      \"stats_24hour\":{\n" +
                "         \"open\":\"0.00032346\",\n" +
                "         \"high\":\"0.00032487\",\n" +
                "         \"low\":\"0.00030804\",\n" +
                "         \"volume\":\"31494.03\",\n" +
                "         \"last\":\"0.00031692\"\n" +
                "      }\n" +
                "   },\n" +
                "   \"NU-USD\":{\n" +
                "      \"stats_30day\":{\n" +
                "         \"volume\":\"665713029.448057\"\n" +
                "      },\n" +
                "      \"stats_24hour\":{\n" +
                "         \"open\":\"0.5857\",\n" +
                "         \"high\":\"0.5961\",\n" +
                "         \"low\":\"0.57\",\n" +
                "         \"volume\":\"9015557.276546\",\n" +
                "         \"last\":\"0.5821\"\n" +
                "      }\n" +
                "   }" +
                "}";

//        MarketData marketData = mapper.readValue(test, Stat.class);
  //      mapper.readerForMapOf(new ParameterizedTypeReference<HashMap<String, Stat>[]>(){}.getClass()).rea

//        Map<String, Stat> statMap = mapper.readValue(test, new ParameterizedTypeReference<HashMap<String, Stat>[]>(){});
        //Map<String, Stat> statMap =
        JsonNode node = mapper.readTree(test);//, new ParameterizedTypeReference<HashMap<String, Stat>[]>(){});
        System.out.println(node.getNodeType());

        Map<String, Stat> statMap = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            String key = entry.getKey();
            String txt = entry.getValue().toString();
            System.out.println("subnode: " + txt);
            if (txt.length() > 0) {
                Stat nStat = mapper.readValue(txt, Stat.class);
                System.out.println("    " + key + ":  Stat: " + nStat);
                statMap.put(key, nStat);
            }
        }
        System.out.println(node);
//        assertEquals(marketData.getAsks().size(), 2);
//        assertEquals(marketData.getBids().size(), 2);
//        assertEquals(marketData.getSequence(), 3L);
    }

    /**
     * This is now out of date - the api delivers a list of strings - amount, price, order Id. NOT number of orders as this test shows.
     * @throws IOException
     */
    @Test
    public void testRawStatsConnection() throws IOException {
        String PUBLIC_KEY = "d8251a21d80b8dd033df34ac6889b8f0";
        String PRIVATE_KEY = "QCpQwJswiawE9BBL3HKnLFFtGOpiwDAnyNzrLGPiF/4PcojLkdxVtF04ILxk2gi6IDT6e45jlRI2HoZ9XsDGaA==";
        String PASSCODE = "ipxlr196ord";
        String URI_SANDBOX="https://api-public.sandbox.pro.coinbase.com";
        String URI_PROD = "https://api.pro.coinbase.com";
        MarketDataService mds = new MarketDataService(new CoinbaseExchangeImpl(PUBLIC_KEY, PASSCODE, URI_PROD,new Signature(PRIVATE_KEY), new ObjectMapper()));
        System.out.println("RESULT WAS: " + mds.getStats());
    }
    /**
     * This is now out of date - the api delivers a list of strings - amount, price, order Id. NOT number of orders as this test shows.
     * @throws IOException
     */
    @Test
    public void testDesirializationOfStatObject() throws IOException {
        String test = "{\n" +
                "      \"stats_30day\":{\n" +
                "         \"volume\":\"1495740.57\"\n" +
                "      },\n" +
                "      \"stats_24hour\":{\n" +
                "         \"open\":\"0.00032346\",\n" +
                "         \"high\":\"0.00032487\",\n" +
                "         \"low\":\"0.00030804\",\n" +
                "         \"volume\":\"31494.03\",\n" +
                "         \"last\":\"0.00031692\"\n" +
                "      }\n" +
                "   },\n";

        Stat stat = mapper.readValue(test, Stat.class);
        System.out.println(stat);
        Stat30Day stats_30day = stat.getStats_30day();
        Stat24Hour stats_24hour = stat.getStats_24hour();

        final BigDecimal VOLUME_EPSILON = new BigDecimal(0.0000001);
        final BigDecimal PRICE_EPSILON = new BigDecimal(0.00000000001);

        assertTrue(closeEnough(stats_30day.getVolume(), new BigDecimal(1495740.57), VOLUME_EPSILON), "30day volume");
        assertTrue(closeEnough(stats_24hour.getVolume(), new BigDecimal(31494.03), VOLUME_EPSILON), "24hour volume");
        assertTrue(closeEnough(stats_24hour.getOpen(), new BigDecimal(0.00032346), PRICE_EPSILON), "24hour volume");
        assertTrue(closeEnough(stats_24hour.getHigh(), new BigDecimal(0.00032487), PRICE_EPSILON), "24hour high");
        assertTrue(closeEnough(stats_24hour.getLow(), new BigDecimal(0.00030804), PRICE_EPSILON), "24hour low");
        assertTrue(closeEnough(stats_24hour.getLast(), new BigDecimal(0.00031692), PRICE_EPSILON), "24hour last");
    }

    private static boolean closeEnough(BigDecimal a, BigDecimal b, BigDecimal epsilon) {
        //return (a-b).
        return a.subtract(b).abs().compareTo(epsilon) < 0;
    }

}