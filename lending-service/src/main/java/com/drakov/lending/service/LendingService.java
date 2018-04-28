package com.drakov.lending.service;

import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import com.drakov.lending.repository.OfferRepository;
import com.drakov.lending.service.finder.BestOffersFinder;
import com.drakov.lending.service.repayment.BorrowerLoanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.drakov.lending.constants.LendingConstants.ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM;
import static com.drakov.lending.constants.LendingConstants.NO_OFFER_WAS_FOUND_EM;

@Component
public class LendingService {

    private static final Logger log = LoggerFactory.getLogger(LendingService.class);

    private OfferRepository offerRepository;
    private BestOffersFinder bestOffersFinder;
    private BorrowerLoanProcessor borrowerLoanProcessor;

    @Autowired
    public LendingService(OfferRepository offerRepository, BestOffersFinder bestOffersFinder, BorrowerLoanProcessor borrowerLoanProcessor) {
        this.offerRepository = offerRepository;
        this.bestOffersFinder = bestOffersFinder;
        this.borrowerLoanProcessor = borrowerLoanProcessor;
    }

    public LendingResponse calculateLoan(double loanAmount) throws UserException {

        log.debug("Loan calculation starts. Amount: " + loanAmount);

        checkAmount(loanAmount);

        List<Offer> allOffers = offerRepository.findAll();

        if (CollectionUtils.isEmpty(allOffers))
            throw new UserException(NO_OFFER_WAS_FOUND_EM);

        Offer bestOffer = bestOffersFinder.findOffer(allOffers, loanAmount);

        if (bestOffer == null) {
            throw new UserException(NO_OFFER_WAS_FOUND_EM);
        }

        LendingResponse response = borrowerLoanProcessor.calculateQuote(bestOffer, loanAmount);

        log.debug("Loan calculation ends. Response: " + response);

        return response;
    }


    private static void checkAmount(double amount) throws UserException {
        if (amount <= 0)
            throw new UserException(ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM, amount);
    }
}
