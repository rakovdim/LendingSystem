package com.drakov.lending.service.repayment;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.model.Lender;
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

    public LendingResponse calculateQuote(Lender lender, double loanAmount) {

        RepaymentCalculator calculator = repaymentCalcProvider.get(lender);

        if (calculator == null)
            throw new InternalProcessingException("No Repayment Calculator found for lender: " + lender.getId());//getName

        RepaymentCalcRequest calcRequest = createCalcRequest(loanAmount, lender.getRate(), loanProperties.getTermInMonths());

        Repayment repayment = calculator.calculate(calcRequest);

        return LendingResponse.newOne()
                .setRequestedAmount(loanAmount)
                .setRate(lender.getRate())
                .setMonthlyRepayment(repayment.getMonthly())
                .setTotalRepayment(repayment.getTotal());
    }

    protected RepaymentCalcRequest createCalcRequest(double loanAmount, double rate, int termInMonths) {
        return new RepaymentCalcRequest(loanAmount, rate, termInMonths);
    }
}