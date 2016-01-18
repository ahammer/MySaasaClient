package com.mysassa.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Adam on 4/3/2015.
 */
public class Tax implements Serializable {
    public Tax() {}
    public  long id;
    public  String name;
    public  BigDecimal rate;
    public  String locale;   //The ISO location code, CA = Canada, CA-BC = Canada, BC, etc.

    public BigDecimal getRate() {
        return rate;
    }
}
