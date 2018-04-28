package com.drakov.lending.service.repayment;

import com.drakov.lending.TestUtils;
import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.model.Lender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BorrowerLoanProcessorTest {

    private static final int TERM_IN_MONTHS = 36;

    private BorrowerLoanProcessor borrowerLoanProcessor;

    @Mock
    private RepaymentCalcProvider repaymentCalcProvider;
    @Mock
    private RepaymentCalculator repaymentCalculator;
    @Mock
    private LoanProperties loanProperties;

    @Before
    public void setUp() {
        this.borrowerLoanProcessor = new BorrowerLoanProcessor(repaymentCalcProvider, loanProperties);
    }

    @Test
    public void testCalculateQuote_shouldReturnCorrectCalculationData_ifAllParametersValid() {

        double amount = 100;
        double rate = 0.11f;
        double expectedCalcMonthly = 100;
        double expectedCalcTotal = 3800;

        Lender lender = TestUtils.mockLenderRate(rate);
        RepaymentCalcRequest expectedRequest = new RepaymentCalcRequest(amount, rate, TERM_IN_MONTHS);

        when(loanProperties.getTermInMonths()).thenReturn(TERM_IN_MONTHS);
        when(repaymentCalcProvider.get(lender)).thenReturn(repaymentCalculator);
        when(repaymentCalculator.calculate(expectedRequest)).thenReturn(new Repayment(expectedCalcMonthly, expectedCalcTotal));

        BorrowerLoanProcessor spyBorrowerLoanProcessor = spy(borrowerLoanProcessor);
        when(spyBorrowerLoanProcessor.createCalcRequest(amount, rate, TERM_IN_MONTHS)).thenReturn(expectedRequest);

        LendingResponse actualResponse = spyBorrowerLoanProcessor.calculateQuote(lender, amount);


        verify(loanProperties, times(1)).getTermInMonths();
        verify(repaymentCalcProvider, times(1)).get(lender);
        verify(repaymentCalculator, times(1)).calculate(expectedRequest);

        assertEquals("Loan amount is not correct", amount, actualResponse.getRequestedAmount(), 0);
        assertEquals("Rate is not correct", rate, actualResponse.getRate(), 0);
        assertEquals("Monthly Repayment is not correct", expectedCalcMonthly, actualResponse.getMonthlyRepayment(), 0);
        assertEquals("Total Repayment is not correct", expectedCalcTotal, actualResponse.getTotalRepayment(), 0);
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculateQuote_shouldThrowException_ifNoCalculatorFound() {

        double amount = 100;
        Lender lender = mock(Lender.class);

        when(repaymentCalcProvider.get(any())).thenReturn(null);

        borrowerLoanProcessor.calculateQuote(lender, amount);

        fail("Exception is not thrown but no calculator was found");

    }

}