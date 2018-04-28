package com.drakov.lending.service.repayment;

import com.drakov.lending.service.repayment.impl.InterestBasedCalculator;
import com.drakov.lending.service.repayment.impl.ZeroRateCalculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static com.drakov.lending.TestUtils.mockOfferRate;
import static org.junit.Assert.assertSame;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class RepaymentCalcProviderTest {
    @Mock
    private InterestBasedCalculator interestBasedCalculator;
    @Mock
    private ZeroRateCalculator zeroRateCalculator;

    private RepaymentCalcProvider repaymentCalcProvider;

    @Before
    public void setUp() throws Exception {
        this.repaymentCalcProvider = new RepaymentCalcProvider(interestBasedCalculator, zeroRateCalculator);
    }

    @Test
    public void testGet_shouldReturnZeroCalc_ifRateIs0() {

        RepaymentCalculator calculator = repaymentCalcProvider.get(mockOfferRate(0));

        assertSame("Not Zero Calc was returned", zeroRateCalculator, calculator);
    }

    @Test
    public void testGet_shouldReturnZeroCalc_ifRateIs10Percents() {

        RepaymentCalculator calculator = repaymentCalcProvider.get(mockOfferRate(0.1f));

        assertSame("Not Interest Based Calc was returned", interestBasedCalculator, calculator);
    }
}