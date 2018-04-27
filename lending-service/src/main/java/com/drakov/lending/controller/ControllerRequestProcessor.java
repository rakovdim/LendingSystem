package com.drakov.lending.controller;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.service.LendingService;
import com.drakov.lending.service.ModelService;
import com.drakov.lending.utils.validation.ArgsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.drakov.lending.constants.LendingConstants.FILE_NOT_FOUND_EM;

/**
 * Created by rakov on 27.04.2018.
 */
@Component
public class ControllerRequestProcessor {
    private static final Logger log = LoggerFactory.getLogger(ControllerRequestProcessor.class);

    private ModelService modelService;
    private LendingService lendingService;
    private LoanProperties props;

    @Autowired
    public ControllerRequestProcessor(ModelService modelService, LendingService lendingService, LoanProperties props) {
        this.modelService = modelService;
        this.lendingService = lendingService;
        this.props = props;
    }

    public LendingResponse uploadFileAndCalculateLoan(String... args) throws UserException {

        ArgsValidator.validateInputArgs(props, args);

        String fileName = args[0];
        double loanAmount = Double.parseDouble(args[1]);

        processMarketFile(fileName);

        return lendingService.calculateLoan(loanAmount);
    }

    protected void processMarketFile(String fileName) throws UserException {
        FileReader reader = null;
        try {
            reader = new FileReader(fileName);

            modelService.uploadModelDataStream(reader);

        } catch (FileNotFoundException e) {
            throw new UserException(FILE_NOT_FOUND_EM, e);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Failed to close file reader stream");
                }
        }
    }
}
