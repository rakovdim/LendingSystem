package com.drakov.lending.service;

import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import com.drakov.lending.repository.OfferRepository;
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
import static com.drakov.lending.constants.LendingConstants.NO_OFFERS_FOUND_DURING_STREAM_PROCESSING_EM;

/**
 * Created by dima on 27.04.18.
 */
@Component
public class ModelService {

    private static final Logger log = LoggerFactory.getLogger(ModelService.class);

    private ModelDataStreamProcessor modelStreamProcessor;
    private OfferRepository offerRepository;

    @Autowired
    public ModelService(ModelDataStreamProcessor modelStreamProcessor, OfferRepository repository) {
        this.modelStreamProcessor = modelStreamProcessor;
        this.offerRepository = repository;
    }

    public void uploadFileWithOffers(String fileName) throws UserException {
        log.debug("Loading file with market values: {0}", fileName);

        FileReader reader = null;
        try {
            reader = new FileReader(fileName);

            uploadOffersStream(reader);

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

    public void uploadOffersStream(Reader reader) throws UserException {

        log.debug("Processing model data stream starts");

        List<Offer> offerList = modelStreamProcessor.uploadOffers(reader);

        if (CollectionUtils.isEmpty(offerList))
            throw new UserException(NO_OFFERS_FOUND_DURING_STREAM_PROCESSING_EM);

        offerList.forEach(offer -> offerRepository.save(offer));

        log.debug("Processing model data stream ends");
    }


}
