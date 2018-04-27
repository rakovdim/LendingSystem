package com.drakov.lending.dto;

import com.drakov.lending.controller.console.format.ResponseFormatter;

public class LendingResponse {
    private double requestedAmount;
    private float rate;
    private double monthlyRepayment;
    private double totalRepayment;

    protected LendingResponse() {
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public float getRate() {
        return rate;
    }

    public float getPercentageRate() {
        return rate * 100;
    }

    public double getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public LendingResponse setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
        return this;
    }

    public LendingResponse setRate(float rate) {
        this.rate = rate;
        return this;
    }

    public LendingResponse setMonthlyRepayment(double monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
        return this;
    }

    public LendingResponse setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
        return this;
    }

    public static LendingResponse newOne() {
        return new LendingResponse();
    }

    public String toString(ResponseFormatter formatter) {
        return formatter.format(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LendingResponse response = (LendingResponse) o;

        if (Double.compare(response.requestedAmount, requestedAmount) != 0) return false;
        if (Float.compare(response.rate, rate) != 0) return false;
        if (Double.compare(response.monthlyRepayment, monthlyRepayment) != 0) return false;
        return Double.compare(response.totalRepayment, totalRepayment) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(requestedAmount);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (rate != +0.0f ? Float.floatToIntBits(rate) : 0);
        temp = Double.doubleToLongBits(monthlyRepayment);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalRepayment);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
