package com.drakov.lending.clients.csv;

import java.io.Reader;

/**
 * Created by rakov on 27.04.2018.
 */
public interface CsvClientFactory {
    public CsvClient create(Reader reader);
}
