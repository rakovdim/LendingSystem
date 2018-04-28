package com.drakov.lending.service.repayment.impl;

import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.service.repayment.Repayment;
import com.drakov.lending.service.repayment.RepaymentCalcRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class InterestBasedCalculatorTest {

    private InterestBasedCalculator calculator;

    @Before
    public void setUp() throws Exception {
        this.calculator = new InterestBasedCalculator();
    }

    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifRateIs0() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(100)
                .setRate(0f)
                .setTermInMonths(10));

        fail("Exception was not thrown but rate is not zero");
    }

    @Test
    public void testCalculate_shouldReturnMonthlyValue_includingInterest() {

        Repayment repayment = calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(3600)
                .setRate(0.1f)
                .setTermInMonths(36));

        assertEquals("Incorrect monthly repayment", 116.16, repayment.getMonthly(), 0.01);
        assertEquals("Incorrect total repayment", 4181.82, repayment.getTotal(), 0.01);
    }
}