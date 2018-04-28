package com.drakov.lending.clients.csv.impl;

import com.drakov.lending.clients.csv.CsvClient;
import com.drakov.lending.clients.csv.CsvClientFactory;
import org.springframework.stereotype.Component;

import java.io.Reader;

/**
 * Created by rakov on 27.04.2018.
 */
@Component
public class OpencsvFactoryImpl implements CsvClientFactory {
    @Override
    public CsvClient create(Reader reader) {
        return new OpencsvClient(reader);
    }
}
