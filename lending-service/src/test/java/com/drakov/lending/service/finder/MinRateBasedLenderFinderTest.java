package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.drakov.lending.TestUtils.mockLenderRateAvailable;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class MinRateBasedLenderFinderTest {

    private MinRateBasedLenderFinder lenderFinder;

    @Before
    public void setUp() throws Exception {
        this.lenderFinder = new MinRateBasedLenderFinder();
    }

    @Test
    public void testFindAppropriateLender_shouldReturnNull_ifLenderListIsEmpty() throws UserException {
        assertNull(lenderFinder.findAppropriateLender(Collections.emptyList(), 100));
    }

    @Test
    public void testFindAppropriateLender_shouldReturnNull_ifLenderListIsNull() throws UserException {
        assertNull(lenderFinder.findAppropriateLender(null, 100));
    }

    @Test
    public void testFindAppropriateLender_shouldReturnNull_ifNoLenderWithRequestedBudget() throws UserException {

        List<Lender> lenders = Arrays.asList(
                mockLenderRateAvailable(0.1f, 100),
                mockLenderRateAvailable(0.2f, 200),
                mockLenderRateAvailable(0.1f, 300));

        Lender result = lenderFinder.findAppropriateLender(lenders, 1000);

        assertNull(result);

    }

    @Test
    public void testFindAppropriateLender_shouldReturnMinRateLender_ifValidLenderExist() throws UserException {

        Lender expectedLender = mockLenderRateAvailable(0.1f, 300);

        List<Lender> lenders = Arrays.asList(
                mockLenderRateAvailable(0.3f, 800),
                mockLenderRateAvailable(0.2f, 300),
                expectedLender);

        Lender result = lenderFinder.findAppropriateLender(lenders, 100);

        assertSame(expectedLender, result);
    }
}