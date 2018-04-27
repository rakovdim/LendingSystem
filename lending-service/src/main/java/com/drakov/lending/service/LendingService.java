package com.drakov.lending.service;

import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.repository.LenderRepository;
import com.drakov.lending.service.finder.LenderFinder;
import com.drakov.lending.service.repayment.BorrowerLoanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.drakov.lending.constants.LendingConstants.LOAN_AMOUNT_NOT_POSITIVE_EM;
import static com.drakov.lending.constants.LendingConstants.NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM;

/**
 * Created by dima on 23.04.18.
 */
@Component
public class LendingService {

    private static final Logger log = LoggerFactory.getLogger(LendingService.class);


    private LenderRepository lenderRepository;
    private LenderFinder lenderFinder;
    private BorrowerLoanProcessor borrowerLoanProcessor;

    @Autowired
    public LendingService(LenderRepository lenderRepository, LenderFinder lenderFinder, BorrowerLoanProcessor borrowerLoanProcessor) {
        this.lenderRepository = lenderRepository;
        this.lenderFinder = lenderFinder;
        this.borrowerLoanProcessor = borrowerLoanProcessor;
    }

    public LendingResponse calculateLoan(double loanAmount) throws UserException {

        log.debug("Loan calculation starts. Amount: " + loanAmount);

        checkAmount(loanAmount);

        List<Lender> allLenders = lenderRepository.findAll();

        if (allLenders == null)
            throw new UserException(NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM);

        LendingResponse response = findLenderAndCalculateQuote(allLenders, loanAmount);

        log.debug("Loan calculation ends. Response: " + response);

        return response;
    }

    protected LendingResponse findLenderAndCalculateQuote(List<Lender> lenders, double loanAmount) throws UserException {

        Lender appropriateLender = lenderFinder.findAppropriateLender(lenders, loanAmount);

        if (appropriateLender == null) {
            throw new UserException(NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM);
        }

        return borrowerLoanProcessor.calculateQuote(appropriateLender, loanAmount);
    }

    private static void checkAmount(double amount) throws UserException {
        if (amount <= 0)
            throw new UserException(LOAN_AMOUNT_NOT_POSITIVE_EM, amount);
    }
}
