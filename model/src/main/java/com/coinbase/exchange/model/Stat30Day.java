package com.coinbase.exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

/**
 * <pre>
 *     {
 *       "id": "BTC-USD",
 *       "base_currency": "BTC",
 *       "quote_currency": "USD",
 *       "base_min_size": "0.001",
 *       "base_max_size": "280",
 *       "base_increment": "0.00000001",
 *       "quote_increment": "0.01",
 *       "display_name": "BTC/USD",
 *       "status": "online",
 *       "margin_enabled": false,
 *       "status_message": "",
 *       "min_market_funds": "5",
 *       "max_market_funds": "1000000",
 *       "post_only": false,
 *       "limit_only": false,
 *       "cancel_only": false,
 *       "type": "spot"
 *     }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stat30Day {
    BigDecimal volume;

    //public Stat30Day(String[] entry) {
    //    this(   new BigDecimal(entry[1]));
    //}

    @ConstructorProperties({"volume"})
    public Stat30Day(BigDecimal volume) {
        this.volume = volume;
    }

    public Stat30Day(String volume) {
        this(new BigDecimal(volume));
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Stat30Day{" +
                "volume=" + volume +
                '}';
    }
}
