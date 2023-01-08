package com.coinbase.exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stat {

    Stat30Day stats_30day;
    Stat24Hour stats_24hour;

    public Stat(String body) {
        System.out.println("getting Stat with just string: " + body);
    }

    public Stat(String[] entry) {
        System.out.print("getting Stat with array of strings: ");
        for (int i=0; i<entry.length; i++) {
            System.out.print("  " + entry[i] + "; ");
        }
        System.out.println("");
        this.stats_30day = new Stat30Day(entry[0]);
        this.stats_24hour = new Stat24Hour(entry);
    }

    @ConstructorProperties({"stats_30day", "stats_24hour"})
    public Stat(Stat30Day stats_30day, Stat24Hour stats_24hour) {
        this.stats_30day = stats_30day;
        this.stats_24hour = stats_24hour;
    }


    public Stat30Day getStats_30day() {
        return stats_30day;
    }

    public void setStats_30day(Stat30Day stats_30day) {
        this.stats_30day = stats_30day;
    }

    public Stat24Hour getStats_24hour() {
        return stats_24hour;
    }

    public void setStats_24hour(Stat24Hour stats_24hour) {
        this.stats_24hour = stats_24hour;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "stats_30day=" + stats_30day +
                ", stats_24hour=" + stats_24hour +
                '}';
    }
}
