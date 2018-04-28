package com.drakov.lending.dto;

public class LendingResponse {
    private double requestedAmount;
    private double rate;
    private double monthlyRepayment;
    private double totalRepayment;

    protected LendingResponse() {
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public double getRate() {
        return rate;
    }

    public double getPercentageRate() {
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

    public LendingResponse setRate(double rate) {
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

    @Override
    public String toString() {
        return "LendingResponse{" +
                "requestedAmount=" + requestedAmount +
                ", rate=" + rate +
                ", monthlyRepayment=" + monthlyRepayment +
                ", totalRepayment=" + totalRepayment +
                '}';
    }
}
