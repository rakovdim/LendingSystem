package com.drakov.lending.controller.console;

import com.drakov.lending.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.text.MessageFormat;
import java.util.Arrays;

import static com.drakov.lending.constants.LendingConstants.REQUEST_ID_MDC_PARAM;

/**
 * Created by rakov on 28.04.2018.
 */
public abstract class AbstractLendingController implements LendingController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLendingController.class);

    protected <T> ResponseEntity<T> processCommonRequest(ProcessingAction<String[], T> processingFunction,
                                                         String... args) {
        try {

            long time = System.currentTimeMillis();

            MDC.put(REQUEST_ID_MDC_PARAM, generateRequestId());

            logger.info("New request processing starts. Args: " + Arrays.toString(args));

            T response = processingFunction.apply(args);

            logger.info(MessageFormat.format("Request successfully processed. Time: {0}ms. Response: {1}",
                    System.currentTimeMillis() - time, response));

            return ResponseEntity.ok(response);

        } catch (UserException e) {
            return handleUserException(e);

        } catch (Exception e) {
            return handleInternalException(e);
        } finally {
            MDC.remove(REQUEST_ID_MDC_PARAM);
        }
    }


    private <T> ResponseEntity<T> handleUserException(UserException e) {
        logException(e);
        return ResponseEntity.userException(e);
    }

    private <T> ResponseEntity<T> handleInternalException(Exception e) {
        logException(e);
        return ResponseEntity.internalError(e);
    }

    //for testing purpose
    protected void logException(Exception e) {
        logger.error(e.getMessage(), e);
    }


    /**
     * For current task this implementation is enough
     */
    private String generateRequestId() {
        return "1";
    }
}
