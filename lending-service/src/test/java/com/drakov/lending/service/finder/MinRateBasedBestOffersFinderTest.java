package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.drakov.lending.TestUtils.mockOfferRateAvailable;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class MinRateBasedBestOffersFinderTest {

    private MinRateBasedBestOffersFinder bestOffersFinder;

    @Before
    public void setUp() throws Exception {
        this.bestOffersFinder = new MinRateBasedBestOffersFinder();
    }

    @Test
    public void testFindOffer_shouldReturnNull_ifOfferListIsEmpty() throws UserException {
        assertNull(bestOffersFinder.findOffer(Collections.emptyList(), 100));
    }

    @Test
    public void testFindOffer_shouldReturnNull_ifOfferListIsNull() throws UserException {
        assertNull(bestOffersFinder.findOffer(null, 100));
    }

    @Test
    public void testFindOffer_shouldReturnNull_ifNoOfferWithRequestedBudget() throws UserException {

        List<Offer> offers = Arrays.asList(
                mockOfferRateAvailable(0.1f, 100),
                mockOfferRateAvailable(0.2f, 200),
                mockOfferRateAvailable(0.1f, 300));

        Offer result = bestOffersFinder.findOffer(offers, 1000);

        assertNull(result);

    }

    @Test
    public void testFindOffer_shouldReturnMinRateOffer_ifValidOfferExist() throws UserException {

        Offer expectedOffer = mockOfferRateAvailable(0.1f, 300);

        List<Offer> offers = Arrays.asList(
                mockOfferRateAvailable(0.3f, 800),
                mockOfferRateAvailable(0.2f, 300),
                expectedOffer);

        Offer result = bestOffersFinder.findOffer(offers, 100);

        assertSame(expectedOffer, result);
    }
}