package com.drakov.lending.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("lending.format")
@Component
public class FormatProperties {
    private int repaymentDecimalPlaces;
    private int rateDecimalPlaces;

    public int getRepaymentDecimalPlaces() {
        return repaymentDecimalPlaces;
    }

    public int getRateDecimalPlaces() {
        return rateDecimalPlaces;
    }

    public void setRepaymentDecimalPlaces(int repaymentDecimalPlaces) {
        this.repaymentDecimalPlaces = repaymentDecimalPlaces;
    }

    public void setRateDecimalPlaces(int rateDecimalPlaces) {
        this.rateDecimalPlaces = rateDecimalPlaces;
    }
}
