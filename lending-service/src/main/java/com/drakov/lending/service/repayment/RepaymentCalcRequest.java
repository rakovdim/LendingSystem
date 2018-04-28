package com.drakov.lending.service.repayment;

/**
 * Created by dima on 25.04.18.
 */
public class RepaymentCalcRequest {
    private double loanAmount;
    private double rate;
    private int termInMonths;

    public RepaymentCalcRequest() {
    }

    public RepaymentCalcRequest(double loanAmount, double rate, int termInMonths) {
        this.loanAmount = loanAmount;
        this.rate = rate;
        this.termInMonths = termInMonths;
    }

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
    public String toString() {
        return "RepaymentCalcRequest{" +
                "loanAmount=" + loanAmount +
                ", rate=" + rate +
                ", termInMonths=" + termInMonths +
                '}';
    }
}
