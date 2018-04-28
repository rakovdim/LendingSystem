package com.drakov.lending.service;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.repository.LenderRepository;
import com.drakov.lending.service.finder.LenderFinder;
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
import static com.drakov.lending.constants.LendingConstants.NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class LendingServiceTest {
    private LendingService lendingService;

    @Mock
    private LenderRepository lenderRepository;
    @Mock
    private LenderFinder lenderFinder;
    @Mock
    private BorrowerLoanProcessor borrowerLoanProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.lendingService = new LendingService(lenderRepository, lenderFinder, borrowerLoanProcessor);
    }

    @Test
    public void testCalculateLoan_shouldCallRequiredServices_ifAllParametersValid() throws UserException {

        Double loanAmount = 10d;
        Lender lender = mock(Lender.class);
        List<Lender> lenders = Collections.singletonList(lender);

        when(lenderRepository.findAll()).thenReturn(lenders);
        when(lenderFinder.findAppropriateLender(lenders, loanAmount)).thenReturn(lender);

        lendingService.calculateLoan(loanAmount);

        verify(lenderRepository, times(1)).findAll();
        verify(lenderFinder, times(1)).findAppropriateLender(lenders, loanAmount);
        verify(borrowerLoanProcessor, times(1)).calculateQuote(lender, loanAmount);
    }

    @Test
    public void testCalculateLoan_shouldThrowException_ifNoLenderInRepoExist() throws UserException {
        thrown.expect(UserException.class);
        thrown.expectMessage(NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM);

        Double loanAmount = 10d;
        when(lenderRepository.findAll()).thenReturn(Collections.emptyList());
        when(lenderFinder.findAppropriateLender(anyList(), anyDouble())).thenReturn(mock(Lender.class));

        lendingService.calculateLoan(loanAmount);
    }

    @Test
    public void testCalculateLoan_shouldThrowException_ifNoAppropriateLenderFoundByFinder() throws UserException {

        expectUserException(NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM);

        Double loanAmount = 10d;
        when(lenderRepository.findAll()).thenReturn(Collections.singletonList(mock(Lender.class)));
        when(lenderFinder.findAppropriateLender(anyList(), anyDouble())).thenReturn(null);

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