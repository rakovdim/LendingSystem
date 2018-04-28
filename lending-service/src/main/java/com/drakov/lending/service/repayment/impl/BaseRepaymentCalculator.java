package com.drakov.lending.service.repayment.impl;

import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.service.repayment.Repayment;
import com.drakov.lending.service.repayment.RepaymentCalcRequest;
import com.drakov.lending.service.repayment.RepaymentCalculator;

/**
 * Created by dima on 27.04.18.
 */
public abstract class BaseRepaymentCalculator implements RepaymentCalculator {

    @Override
    public Repayment calculate(RepaymentCalcRequest request) {

        validateRequest(request);

        return performCalculation(request);
    }

    protected abstract Repayment performCalculation(RepaymentCalcRequest request);

    protected boolean isRequestValid(RepaymentCalcRequest request) {
        return request.getLoanAmount() > 0 &&
                request.getTermInMonths() > 0 &&
                !(request.getRate() < 0);
    }

    private void validateRequest(RepaymentCalcRequest request) {

        if (!isRequestValid(request))
            throw new InternalProcessingException("Can't perform calculation. Incorrect request: " + request);
    }
}
