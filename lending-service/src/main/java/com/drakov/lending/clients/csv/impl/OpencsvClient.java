package com.drakov.lending.clients.csv.impl;

import com.drakov.lending.clients.csv.CsvClient;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by rakov on 27.04.2018.
 */
public class OpencsvClient implements CsvClient {

    private Reader reader;
    private CSVReader csvReader;


    public OpencsvClient(Reader reader) {
        this(reader, new CSVReader(reader));
    }

    private OpencsvClient(Reader reader, CSVReader csvReader) {
        this.reader = reader;
        this.csvReader = csvReader;
    }

    @Override
    public String[] readRow() throws IOException {
        return csvReader.readNext();
    }
}
