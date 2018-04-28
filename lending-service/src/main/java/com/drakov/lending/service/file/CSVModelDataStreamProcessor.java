package com.drakov.lending.service.file;

import com.drakov.lending.clients.csv.CsvClient;
import com.drakov.lending.clients.csv.CsvClientFactory;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import com.drakov.lending.model.ModelFactory;
import com.drakov.lending.utils.validation.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVModelDataStreamProcessor implements ModelDataStreamProcessor {

    private static final Logger log = LoggerFactory.getLogger(CSVModelDataStreamProcessor.class);

    private ModelFactory modelFactory;
    private CsvClientFactory csvClientFactory;

    @Autowired
    public CSVModelDataStreamProcessor(ModelFactory modelFactory, CsvClientFactory csvClientFactory) {
        this.modelFactory = modelFactory;
        this.csvClientFactory = csvClientFactory;
    }

    @Override
    public List<Offer> uploadOffers(Reader reader) throws UserException {
        try {
            CsvClient csvClient = csvClientFactory.create(reader);

            return processCSVFile(csvClient);

        } catch (IOException e) {
            throw new InternalProcessingException(e);
        }
    }

    private List<Offer> processCSVFile(CsvClient csvClient) throws IOException, UserException {

        String[] headers = csvClient.readRow();

        FileValidator.validateHeaders(headers);

        log.trace("Header is read and validated. Starting reading values");

        List<Offer> offers = new ArrayList<>();

        String[] offerRow;
        while ((offerRow = csvClient.readRow()) != null) {

            FileValidator.validateValues(offerRow);

            Offer offer = createOffer(offerRow[0], offerRow[1], offerRow[2]);

            log.trace("Values is validated and Offer is created: {0}", offer);

            offers.add(offer);
        }
        return offers;
    }

    private Offer createOffer(String name, String rate, String available) {
        return modelFactory.createOffer(name, Double.parseDouble(rate), Double.parseDouble(available));
    }

}

