package com.drakov.lending.service.repayment.impl;

import com.drakov.lending.service.repayment.Repayment;
import com.drakov.lending.service.repayment.RepaymentCalcRequest;
import org.springframework.stereotype.Component;

/**
 * Created by dima on 26.04.18.
 */
@Component
public class ZeroRateCalculator extends BaseRepaymentCalculator {

    @Override
    protected Repayment performCalculation(RepaymentCalcRequest request) {

        double monthlyRepayment = request.getLoanAmount() / request.getTermInMonths();

        return new Repayment(monthlyRepayment, monthlyRepayment * request.getTermInMonths());
    }

    @Override
    protected boolean isRequestValid(RepaymentCalcRequest request) {
        return super.isRequestValid(request) && request.getRate() == 0;
    }
}
