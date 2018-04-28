package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by dima on 24.04.18.
 */
@Component
public class MinRateBasedBestOffersFinder implements BestOffersFinder {

    @Override
    public Offer findOffer(List<Offer> offers, double loanAmount) throws UserException {

        if (CollectionUtils.isEmpty(offers))
            return null;

        Optional<Offer> offerOptional = offers.stream().
                filter(offer -> offer.getAvailable() >= loanAmount).
                min((offer1, offer2) -> Double.compare(offer1.getRate(), offer2.getRate()));

        return offerOptional.isPresent() ? offerOptional.get() : null;
    }
}