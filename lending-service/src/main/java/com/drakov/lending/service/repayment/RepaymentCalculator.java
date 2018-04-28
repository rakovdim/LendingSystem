package com.drakov.lending.service.repayment;


/**
 * Created by dima on 24.04.18.
 */
public interface RepaymentCalculator {

    public Repayment calculate(RepaymentCalcRequest request);
}
