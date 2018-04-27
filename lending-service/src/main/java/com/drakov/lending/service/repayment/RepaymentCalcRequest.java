package com.drakov.lending.service.repayment;

/**
 * Created by dima on 25.04.18.
 */
public class RepaymentCalcRequest {
    private double loanAmount;
    private double rate;
    private int termInMonths;

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getRate() {
        return rate;
    }

    public int getTermInMonths() {
        return termInMonths;
    }

    public RepaymentCalcRequest setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public RepaymentCalcRequest setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public RepaymentCalcRequest setTermInMonths(int termInMonths) {
        this.termInMonths = termInMonths;
        return this;
    }

    public static RepaymentCalcRequest newOne() {
        return new RepaymentCalcRequest();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepaymentCalcRequest that = (RepaymentCalcRequest) o;

        if (Double.compare(that.loanAmount, loanAmount) != 0) return false;
        if (Double.compare(that.rate, rate) != 0) return false;
        return termInMonths == that.termInMonths;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(loanAmount);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + termInMonths;
        return result;
    }
}
