package com.drakov.lending.service.repayment;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dima on 27.04.18.
 */
@Component
public class BorrowerLoanProcessor {

    private RepaymentCalcProvider repaymentCalcProvider;
    private LoanProperties loanProperties;

    @Autowired
    public BorrowerLoanProcessor(RepaymentCalcProvider repaymentCalcProvider, LoanProperties loanProperties) {
        this.repaymentCalcProvider = repaymentCalcProvider;
        this.loanProperties = loanProperties;
    }

    public LendingResponse calculateQuote(Offer offer, double loanAmount) {

        RepaymentCalculator calculator = repaymentCalcProvider.get(offer);

        if (calculator == null)
            throw new InternalProcessingException("No Repayment Calculator found for offer: " + offer.getId());//getLender

        RepaymentCalcRequest calcRequest = createCalcRequest(loanAmount, offer.getRate(), loanProperties.getTermInMonths());

        Repayment repayment = calculator.calculate(calcRequest);

        return LendingResponse.newOne()
                .setRequestedAmount(loanAmount)
                .setRate(offer.getRate())
                .setMonthlyRepayment(repayment.getMonthly())
                .setTotalRepayment(repayment.getTotal());
    }

    protected RepaymentCalcRequest createCalcRequest(double loanAmount, double rate, int termInMonths) {
        return new RepaymentCalcRequest(loanAmount, rate, termInMonths);
    }
}
