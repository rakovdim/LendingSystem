package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by dima on 24.04.18.
 */
@Component
public class MinRateBasedLenderFinder implements LenderFinder {

    @Override
    public Lender findAppropriateLender(List<Lender> lenders, double loanAmount) throws UserException {
        Optional<Lender> lenderOptional = lenders.stream().
                filter(lender -> lender.getAvailable() >= loanAmount).
                min((lender1, lender2) -> Float.compare(lender1.getRate(), lender2.getRate()));

        return lenderOptional.isPresent() ? lenderOptional.get() : null;
    }
}