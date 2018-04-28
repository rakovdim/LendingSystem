package com.drakov.lending.repository;

import com.drakov.lending.model.Offer;

import java.util.List;

public interface OfferRepository {

    public void save(Offer offer);

    public List<Offer> findAll();
}
