package com.drakov.lending.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties("lending.loan")
@Component
public class LoanProperties {

    private int termInMonths;
    private double maxValue;
    private double minValue;
    private int loanAmountMultiple;

    public int getTermInMonths() {
        return termInMonths;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public int getLoanAmountMultiple() {
        return loanAmountMultiple;
    }

    public void setLoanAmountMultiple(int loanAmountMultiple) {
        this.loanAmountMultiple = loanAmountMultiple;
    }
}
