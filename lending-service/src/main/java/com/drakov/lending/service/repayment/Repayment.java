package com.drakov.lending.service.repayment;

/**
 * Created by dima on 26.04.18.
 */
public class Repayment {
    private double monthly;
    private double total;

    public Repayment(double monthly, double total) {
        this.monthly = monthly;
        this.total = total;
    }

    public double getMonthly() {
        return monthly;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Repayment{" +
                "monthly=" + monthly +
                ", total=" + total +
                '}';
    }
}