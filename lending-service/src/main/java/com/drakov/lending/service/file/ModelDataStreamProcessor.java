package com.drakov.lending.service.file;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;

import java.io.Reader;
import java.util.List;

/**
 * Created by dima on 24.04.18.
 */

public interface ModelDataStreamProcessor {

    public List<Offer> uploadOffers(Reader reader) throws UserException;
}

