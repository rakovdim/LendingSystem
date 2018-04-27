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
public class ZeroRateCalculatorTest {

    private ZeroRateCalculator calculator;

    @Before
    public void setUp() throws Exception {
        this.calculator = new ZeroRateCalculator();
    }


    @Test(expected = InternalProcessingException.class)
    public void testCalculate_shouldThrowException_ifRateIsNot0() {

        calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(100)
                .setRate(0.1f)
                .setTermInMonths(10));

        fail("Exception was not thrown but rate is not zero");
    }

    @Test
    public void testCalculate_shouldReturnMonthly100_ifTermIs36AmountIs3600() {

        double amount = 3600;

        Repayment repayment = calculator.calculate(RepaymentCalcRequest.newOne()
                .setLoanAmount(amount)
                .setRate(0)
                .setTermInMonths(36));

        assertEquals("Incorrect monthly repayment", 100, repayment.getMonthly(), 0);
        assertEquals("Incorrect total repayment", amount, repayment.getTotal(), 0);
    }
}