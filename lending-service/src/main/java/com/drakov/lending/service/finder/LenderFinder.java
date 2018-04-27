package com.drakov.lending.service.finder;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;

import java.util.List;

/**
 * Created by dima on 24.04.18.
 */
public interface LenderFinder {
    public Lender findAppropriateLender(List<Lender> lenders, double loanAmount) throws UserException;
}
