package com.mysassa.api.model;

import com.google.gson.JsonObject;

import java.math.BigDecimal;

/**
 * Created by Adam on 2/20/2015.
 */
public class BitcoinPriceData {
    public final BigDecimal avg,ask,bid,last,volume_btc,volume_percent;

    @Override
    public String toString() {
        return "BitcoinPriceData{" +
                "avg=" + avg +
                ", ask=" + ask +
                ", bid=" + bid +
                ", last=" + last +
                ", volume_btc=" + volume_btc +
                ", volume_percent=" + volume_percent +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public final String timestamp;

    public BitcoinPriceData(JsonObject obj) {
        avg = obj.get("avg").getAsBigDecimal();
        ask = obj.get("ask").getAsBigDecimal();
        bid = obj.get("bid").getAsBigDecimal();
        last = obj.get("last").getAsBigDecimal();
        timestamp = obj.get("timestamp").getAsString();
        volume_btc = obj.get("volume_btc").getAsBigDecimal();
        volume_percent = obj.get("volume_percent").getAsBigDecimal();
    }
}
