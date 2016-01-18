package com.metalrain.simple.api.model;

import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Created by Adam on 2/20/2015.
 */
public class BitcoinTransactionData {
    public final BitcoinPriceData quotedPrice;
    public final Date date;
    public final String transaction_id;

    public BitcoinTransactionData(JsonObject json) {
        date = new Date(json.get("date").getAsString());
        transaction_id = json.get("transaction_id").getAsString();
        quotedPrice = new BitcoinPriceData(json.getAsJsonObject("quotedPrice"));
    }

    @Override
    public String toString() {
        return "BitcoinTransactionData{" +

                ", quotedPrice=" + quotedPrice +
                ", date=" + date +
                ", transaction_id='" + transaction_id + '\'' +
                '}';
    }
}
