package com.coinbase.exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.Instant;

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
public class Stat24Hour {
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    private BigDecimal last;

    public Stat24Hour(String[] entry) {
        this(   new BigDecimal(entry[1]),
                new BigDecimal(entry[2]),
                new BigDecimal(entry[3]),
                new BigDecimal(entry[4]),
                new BigDecimal(entry[5]));
    }

    @ConstructorProperties({"open", "high", "low", "volume", "last"})
    public Stat24Hour(String open, String high, String low, String volume, String last) {
        this.open = new BigDecimal(open);
        this.high = new BigDecimal(high);
        this.low = new BigDecimal(low);
        this.volume = new BigDecimal(volume);
        this.last = new BigDecimal(last);
    }

    @ConstructorProperties({"open", "high", "low", "volume", "last"})
    public Stat24Hour(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal volume, BigDecimal last) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.last = last;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLast() {
        return last;
    }

    public void setLast(BigDecimal last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Stat24Hour{" +
                "open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", last=" + last +
                '}';
    }
}
