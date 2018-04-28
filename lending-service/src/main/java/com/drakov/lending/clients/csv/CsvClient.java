package com.drakov.lending.clients.csv;

import java.io.IOException;

/**
 * Created by rakov on 27.04.2018.
 */
public interface CsvClient {
    public String[] readRow() throws IOException;
}
