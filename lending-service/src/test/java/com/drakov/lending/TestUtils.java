package com.drakov.lending;


import com.drakov.lending.model.Offer;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static Offer mockEmptyOffer() {
        return mock(Offer.class);
    }

    public static Offer mockOfferRate(double rate) {
        return doMock(offer -> when(offer.getRate()).thenReturn(rate));
    }

    public static Offer mockOfferAvailable(double available) {
        return doMock(offer -> when(offer.getAvailable()).thenReturn(available));
    }

    public static Offer mockOfferRateAvailable(double rate, double available) {
        return doMock(offer -> {
            when(offer.getAvailable()).thenReturn(available);
            when(offer.getRate()).thenReturn(rate);
        });
    }

    public static Offer mockOfferAll(String name, double rate, double available) {
        return doMock(offer -> {
            when(offer.getRate()).thenReturn(rate);
            when(offer.getAvailable()).thenReturn(available);
            when(offer.getLender()).thenReturn(name);
        });
    }

    private static Offer doMock(Consumer<Offer> consumer) {
        Offer offer = mockEmptyOffer();
        consumer.accept(offer);
        return offer;
    }
}
