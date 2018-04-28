package com.drakov.lending.service;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import com.drakov.lending.repository.OfferRepository;
import com.drakov.lending.service.finder.BestOffersFinder;
import com.drakov.lending.service.repayment.BorrowerLoanProcessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static com.drakov.lending.constants.LendingConstants.ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM;
import static com.drakov.lending.constants.LendingConstants.NO_OFFER_WAS_FOUND_EM;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class LendingServiceTest {
    private LendingService lendingService;

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private BestOffersFinder bestOffersFinder;
    @Mock
    private BorrowerLoanProcessor borrowerLoanProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.lendingService = new LendingService(offerRepository, bestOffersFinder, borrowerLoanProcessor);
    }

    @Test
    public void testCalculateLoan_shouldCallRequiredServices_ifAllParametersValid() throws UserException {

        Double loanAmount = 10d;
        Offer offer = mock(Offer.class);
        List<Offer> offers = Collections.singletonList(offer);

        when(offerRepository.findAll()).thenReturn(offers);
        when(bestOffersFinder.findOffer(offers, loanAmount)).thenReturn(offer);

        lendingService.calculateLoan(loanAmount);

        verify(offerRepository, times(1)).findAll();
        verify(bestOffersFinder, times(1)).findOffer(offers, loanAmount);
        verify(borrowerLoanProcessor, times(1)).calculateQuote(offer, loanAmount);
    }

    @Test
    public void testCalculateLoan_shouldThrowException_ifNoOfferInRepoExist() throws UserException {
        thrown.expect(UserException.class);
        thrown.expectMessage(NO_OFFER_WAS_FOUND_EM);

        Double loanAmount = 10d;
        when(offerRepository.findAll()).thenReturn(Collections.emptyList());
        when(bestOffersFinder.findOffer(anyList(), anyDouble())).thenReturn(mock(Offer.class));

        lendingService.calculateLoan(loanAmount);
    }

    @Test
    public void testCalculateLoan_shouldThrowException_ifNoBestOfferFoundByFinder() throws UserException {

        expectUserException(NO_OFFER_WAS_FOUND_EM);

        Double loanAmount = 10d;
        when(offerRepository.findAll()).thenReturn(Collections.singletonList(mock(Offer.class)));
        when(bestOffersFinder.findOffer(anyList(), anyDouble())).thenReturn(null);

        lendingService.calculateLoan(loanAmount);
    }

    @Test
    public void testCalculateLoan_shouldThrowUserException_ifLoanAmountIsNegative() throws UserException {

        expectUserException(MessageFormat.format(ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM, -1));

        lendingService.calculateLoan(-1);
    }

    @Test
    public void testCalculateLoan_shouldThrowUserException_ifLoanAmountIsZero() throws UserException {

        expectUserException(MessageFormat.format(ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM, 0));

        lendingService.calculateLoan(0);
    }

    private void expectUserException(String message) {
        thrown.expect(UserException.class);
        thrown.expectMessage(message);

    }
}