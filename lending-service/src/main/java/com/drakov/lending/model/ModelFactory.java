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

    public Lender createLender(String name, float rate, double available) {
        return new Lender(idGenerator.generateId(), name, rate, available);
    }
}
