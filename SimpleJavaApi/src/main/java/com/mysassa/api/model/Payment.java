package com.mysassa.api.model;

import com.google.gson.JsonObject;
import com.mysassa.api.enums.Currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by adam on 2014-10-31.
 */
public class Payment implements Serializable{
    public final  Date dateReceived =  new Date();
    public final BigDecimal value;
    public final Currency currency;
    public final Boolean validated;
    public final Boolean successful;

    public final BitcoinTransactionData bitcoinTransactionData;


    public Payment(JsonObject json) {

        System.out.println("Constructing: "+json);
        value = json.get("value").getAsBigDecimal();
        currency = Currency.fromString(json.get("currency").getAsString());
        validated = json.get("validated").getAsBoolean();
        successful= json.get("successful").getAsBoolean();
        bitcoinTransactionData = new BitcoinTransactionData(json.get("bitcoinTransactionData").getAsJsonObject());
    }

    public BigDecimal calculateNativeValue() {
        if (bitcoinTransactionData == null) {
            return value;
        } else {
            return bitcoinTransactionData.quotedPrice.avg.multiply(value);
        }
    }


}
