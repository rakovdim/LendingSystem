package com.drakov.lending.service.repayment.impl;

import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.service.repayment.Repayment;
import com.drakov.lending.service.repayment.RepaymentCalcRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class BaseRepaymentCalculatorTest {

    private BaseRepaymentCalculator calculator;
    @Mock
    private Repayment expectedRepayment;

    @Before
    public void setUp() throws Exception {
        this.calculator = mock(BaseRepaymentCalculator.class, CALLS_REAL_METHODS);
        doReturn(expectedRepayment).when(calculator).performCalculation(any());

    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifTermInMonthsIs0() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(100)
                .setRate(0)
                .setTermInMonths(0));

        fail("Exception was not thrown but term is 0");
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifTermInMonthsIsNegative() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(100)
                .setRate(0)
                .setTermInMonths(-10));

        fail("Exception was not thrown but term is negative");
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifLoanAmountIs0() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(0)
                .setRate(0)
                .setTermInMonths(10));

        fail("Exception was not thrown but loan amount is 0");
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifLoanAmountIsNegative() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(-10)
                .setRate(0)
                .setTermInMonths(10));

        fail("Exception was not thrown but loan amount is negative");
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifRateIsNegative() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(1000)
                .setRate(-0.12f)
                .setTermInMonths(10));

        fail("Exception was not thrown but rate is negative");
    }

    @Test
    public void testCalculate_shouldCallCalculation_ifArgsValid() {
        RepaymentCalcRequest request = RepaymentCalcRequest.newOne()
                .setLoanAmount(1000)
                .setRate(0.12f)
                .setTermInMonths(10);

        Repayment repayment = calculator.calculate(request);

        verify(calculator, times(1)).performCalculation(request);
        assertSame(expectedRepayment, repayment);
    }
}