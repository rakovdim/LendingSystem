package com.drakov.lending.model;

import com.drakov.lending.utils.id.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelFactory {
    private IdGenerator idGenerator;

    @Autowired
    public ModelFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Offer createOffer(String offer, double rate, double available) {
        return new Offer(idGenerator.generateId(), offer, rate, available);
    }
}
