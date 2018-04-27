package com.drakov.lending;


import com.drakov.lending.model.Lender;

import static com.drakov.lending.TestConstants.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static Lender mockLender() {
        return mockLender(DEFAULT_LENDER_RATE);
    }

    public static Lender mockLender(float rate) {
        return mockLender(rate, DEFAULT_LENDER_AVAILABLE);
    }

    public static Lender mockLender(float rate, double available) {
        return mockLender(DEFAULT_LENDER_NAME, rate, available);
    }

    public static Lender mockLender(String name, float rate, double available) {
        Lender lender = mock(Lender.class);
        when(lender.getRate()).thenReturn(rate);
        when(lender.getAvailable()).thenReturn(available);
        when(lender.getName()).thenReturn(name);
        return lender;
    }
}
