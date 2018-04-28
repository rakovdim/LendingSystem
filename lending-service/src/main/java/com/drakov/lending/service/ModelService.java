package com.drakov.lending.service;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.repository.LenderRepository;
import com.drakov.lending.service.file.ModelDataStreamProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static com.drakov.lending.constants.LendingConstants.FILE_NOT_FOUND_EM;
import static com.drakov.lending.constants.LendingConstants.NO_LENDERS_FOUND_DURING_STREAM_PROCESSING_EM;

/**
 * Created by dima on 27.04.18.
 */
@Component
public class ModelService {

    private static final Logger log = LoggerFactory.getLogger(ModelService.class);

    private ModelDataStreamProcessor modelStreamProcessor;
    private LenderRepository lenderRepository;

    @Autowired
    public ModelService(ModelDataStreamProcessor modelStreamProcessor, LenderRepository repository) {
        this.modelStreamProcessor = modelStreamProcessor;
        this.lenderRepository = repository;
    }

    public void uploadModelDataFile(String fileName) throws UserException {
        log.debug("Loading file with market values: {0}", fileName);

        FileReader reader = null;
        try {
            reader = new FileReader(fileName);

            uploadModelDataStream(reader);

        } catch (FileNotFoundException e) {
            log.error("File was not wound: " + fileName);

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

    public void uploadModelDataStream(Reader reader) throws UserException {

        log.debug("Processing model data stream starts");

        List<Lender> lenderList = modelStreamProcessor.uploadStreamData(reader);

        if (CollectionUtils.isEmpty(lenderList))
            throw new UserException(NO_LENDERS_FOUND_DURING_STREAM_PROCESSING_EM);

        lenderList.forEach(lender -> lenderRepository.save(lender));

        log.debug("Processing model data stream ends");
    }


}
