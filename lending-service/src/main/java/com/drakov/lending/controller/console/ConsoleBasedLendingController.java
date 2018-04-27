package com.drakov.lending.controller.console;

import com.drakov.lending.controller.ControllerRequestProcessor;
import com.drakov.lending.controller.console.format.ResponseFormatter;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.drakov.lending.constants.LendingConstants.INTERNAL_EXCEPTION_OCCURRED_EM;
import static com.drakov.lending.constants.LendingConstants.REQUEST_ID_MDC_PARAM;

@Component
public class ConsoleBasedLendingController implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleBasedLendingController.class);

    private ControllerRequestProcessor requestProcessor;
    private ResponseFormatter responseFormatter;

    @Autowired
    public ConsoleBasedLendingController(ControllerRequestProcessor requestProcessor, ResponseFormatter responseFormatter) {
        this.requestProcessor = requestProcessor;
        this.responseFormatter = responseFormatter;
    }

    @Override
    public void run(String... args) {
        try {

            MDC.put(REQUEST_ID_MDC_PARAM, generateRequestId());

            logger.info("New request processing starts. Args: " + Arrays.toString(args));

            LendingResponse lendingResponse = requestProcessor.uploadFileAndCalculateLoan(args);

            String formattedResponse = responseFormatter.format(lendingResponse);

            System.out.println(formattedResponse);

            logger.info("Request processing successfully ends. Response: \n" + formattedResponse);

        } catch (UserException e) {
            handleUserException(e);

        } catch (Exception e) {
            handleInternalException(e);
        } finally {
            MDC.remove(REQUEST_ID_MDC_PARAM);
        }
    }


    protected void handleUserException(UserException e) {
        handleException(e, e.getMessage(), e.getMessage());
    }

    protected void handleInternalException(Exception e) {
        handleException(e, e.getMessage(), INTERNAL_EXCEPTION_OCCURRED_EM);
    }

    private static void handleException(Exception e, String logMessage, String userMessage) {
        logger.error(logMessage, e);
        System.out.println(userMessage);
    }

    /**
     * For current task this implementation is enough
     */
    private String generateRequestId() {
        return "1";
    }
}