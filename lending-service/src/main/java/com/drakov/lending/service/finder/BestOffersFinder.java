package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;

import java.util.List;

/**
 * Created by dima on 24.04.18.
 */
public interface BestOffersFinder {
    public Offer findOffer(List<Offer> offers, double loanAmount) throws UserException;
}
