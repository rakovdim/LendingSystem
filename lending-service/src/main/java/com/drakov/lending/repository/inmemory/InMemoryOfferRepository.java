package com.drakov.lending.repository.inmemory;

import com.drakov.lending.model.Offer;
import com.drakov.lending.repository.OfferRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InMemoryOfferRepository implements OfferRepository {
    private List<Offer> offers = new ArrayList<>();

    @Override
    public void save(Offer offer) {
        offers.add(offer);
    }

    @Override
    public List<Offer> findAll() {
        return Collections.unmodifiableList(offers);
    }
}
