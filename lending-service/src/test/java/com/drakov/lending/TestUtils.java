package com.drakov.lending;


import com.drakov.lending.model.Lender;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static Lender mockEmptyLender() {
        return mock(Lender.class);
    }

    public static Lender mockLenderRate(double rate) {
        return doMock(lender -> when(lender.getRate()).thenReturn(rate));
    }

    public static Lender mockLenderAvailable(double available) {
        return doMock(lender -> when(lender.getAvailable()).thenReturn(available));
    }

    public static Lender mockLenderRateAvailable(double rate, double available) {
        return doMock(lender -> {
            when(lender.getAvailable()).thenReturn(available);
            when(lender.getRate()).thenReturn(rate);
        });
    }

    public static Lender mockLenderAll(String name, double rate, double available) {
        return doMock(lender -> {
            when(lender.getRate()).thenReturn(rate);
            when(lender.getAvailable()).thenReturn(available);
            when(lender.getName()).thenReturn(name);
        });
    }

    private static Lender doMock(Consumer<Lender> consumer) {
        Lender lender = mockEmptyLender();
        consumer.accept(lender);
        return lender;
    }
}
