package com.drakov.lending.service.repayment;

import com.drakov.lending.model.Lender;
import com.drakov.lending.service.repayment.impl.InterestBasedCalculator;
import com.drakov.lending.service.repayment.impl.ZeroRateCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dima on 26.04.18.
 */
@Component
public class RepaymentCalcProvider {

    private InterestBasedCalculator interestBasedCalculator;
    private ZeroRateCalculator zeroRateCalculator;

    @Autowired
    public RepaymentCalcProvider(InterestBasedCalculator interestBasedCalculator, ZeroRateCalculator zeroRateCalculator) {
        this.interestBasedCalculator = interestBasedCalculator;
        this.zeroRateCalculator = zeroRateCalculator;
    }

    public RepaymentCalculator get(Lender lender) {
        return lender.getRate() == 0 ? zeroRateCalculator : interestBasedCalculator;
    }
}
