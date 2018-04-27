package com.drakov.lending.service.file;

import com.drakov.lending.clients.csv.CsvClient;

/**
 * Created by rakov on 27.04.2018.
 */
public class CsvClientStub implements CsvClient {
    private String[][] csvData;
    private int currentRow = 0;

    public CsvClientStub(String[][] csvData) {
        this.csvData = csvData;
    }


    @Override
    public String[] readRow() {
        if (csvData == null)
            return null;
        try {
            return csvData[currentRow++];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
