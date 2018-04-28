package com.drakov.lending.repository.inmemory;

import com.drakov.lending.model.Lender;
import com.drakov.lending.repository.LenderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InMemoryLenderRepository implements LenderRepository {
    private List<Lender> lenders = new ArrayList<>();

    @Override
    public void save(Lender lender) {
        lenders.add(lender);
    }

    @Override
    public List<Lender> findAll() {
        return Collections.unmodifiableList(lenders);
    }
}
