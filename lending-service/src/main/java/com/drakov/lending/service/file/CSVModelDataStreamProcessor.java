package com.drakov.lending.service.file;

import com.drakov.lending.clients.csv.CsvClient;
import com.drakov.lending.clients.csv.CsvClientFactory;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.model.ModelFactory;
import com.drakov.lending.utils.validation.FileValidator;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVModelDataStreamProcessor implements ModelDataStreamProcessor {

    private ModelFactory modelFactory;
    private CsvClientFactory csvClientFactory;

    @Autowired
    public CSVModelDataStreamProcessor(ModelFactory modelFactory, CsvClientFactory csvClientFactory) {
        this.modelFactory = modelFactory;
        this.csvClientFactory = csvClientFactory;
    }

    @Override
    public List<Lender> uploadStreamData(Reader reader) throws UserException {
        try {
            CsvClient csvClient = csvClientFactory.create(reader);

            return processCSVFile(csvClient);

        } catch (IOException e) {
            throw new InternalProcessingException(e);
        }
    }

    private List<Lender> processCSVFile(CsvClient csvClient) throws IOException, UserException {

        String[] headers = csvClient.readRow();

        FileValidator.validateFileNotEmpty(headers);
        FileValidator.validateHeaders(headers);

        List<Lender> lenders = new ArrayList<>();

        String[] lenderRow;
        while ((lenderRow = csvClient.readRow()) != null) {

            FileValidator.validateValues(lenderRow);

            Lender lender = createLender(lenderRow[0], lenderRow[1], lenderRow[2]);

            lenders.add(lender);
        }
        return lenders;
    }

    private Lender createLender(String name, String rate, String available) {
        return modelFactory.createLender(name, Float.parseFloat(rate), Double.parseDouble(available));
    }

}

