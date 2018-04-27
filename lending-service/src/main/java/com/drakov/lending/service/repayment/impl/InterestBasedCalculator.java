package com.drakov.lending.service.repayment.impl;

import com.drakov.lending.service.repayment.RepaymentCalculator;
import com.drakov.lending.service.repayment.Repayment;
import com.drakov.lending.service.repayment.RepaymentCalcRequest;
import org.springframework.stereotype.Component;

/**
 * Created by dima on 24.04.18.
 */
@Component
public class InterestBasedCalculator implements RepaymentCalculator {

    @Override
    public Repayment calculate(RepaymentCalcRequest request) {
        double monthlyRepayment = calcMonthlyRepayment(request.getLoanAmount(), request.getRate(), request.getTermInMonths());

        return new Repayment(monthlyRepayment, monthlyRepayment * request.getTermInMonths());
    }

    private double calcMonthlyRepayment(double loanAmount, double rate, int termInMonths) {
        double monthlyRate = rate / 12.0;
        return (loanAmount * monthlyRate) /
                (1 - Math.pow(1 + monthlyRate, -termInMonths));

    }
}
