package com.drakov.lending.repository;

import com.drakov.lending.model.Lender;

import java.util.List;

public interface LenderRepository {

    public void save(Lender lender);

    public List<Lender> findAll();
}
