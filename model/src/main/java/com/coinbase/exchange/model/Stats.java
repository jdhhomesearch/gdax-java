package com.coinbase.exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

    Map<String, Stat> statMap;

//    public Stats(Map<String, Stat> stats) {
//        //this.candleList =
//        stats.stream().map(Stat::new).collect(Collectors.toList());
//    }

//    public Stats(String entry) {
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

//    public Stats(Stat[] entry) {
//       this(   new HashMap<String, Stat>()(entry[1]));
//    }
//
    public Stats(Map<String, Stat> statMap) {
        this.statMap = statMap;
    }

//    public Stats(List<String[]> stats) {
//        this.statMap = stats.stream().map(
//                (String[] stat) -> new Stat(stat)
//        ).collect(Collectors.toList());
//    }

    public Map<String, Stat> getStatMap() {
        return statMap;
    }

}
