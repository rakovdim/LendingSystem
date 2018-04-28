package com.drakov.lending;

import com.drakov.lending.config.FormatProperties;
import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.constants.LendingConstants;
import com.drakov.lending.controller.console.LendingController;
import com.drakov.lending.controller.console.ResponseEntity;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.opencsv.CSVReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.Arrays;

import static com.drakov.lending.constants.LendingConstants.*;
import static com.drakov.lending.controller.console.ResponseEntity.STATUS.OK;
import static com.drakov.lending.controller.console.ResponseEntity.STATUS.USER_EXCEPTION;
import static org.junit.Assert.*;

/**
 * Created by rakov on 28.04.2018.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(value = {"classpath:application-int-test.properties"})
public class LendingServiceIntegrationTest {

    @Value("${input_test_files_dir}")
    private String inputFilesTestDir;

    @Autowired
    private LendingController lendingController;
    @Autowired
    private FormatProperties formatProperties;

    @Test
    public void testProcessLendingRequest_shouldReturnOkResponse_withInterestInvolved() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "market_interest_ok.csv";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(OK, responseEntity.getStatus());
        assertNull(responseEntity.getException());
        assertNotNull(response);

        assertEquals("Incorrect loan amount", loanAmount, response.getRequestedAmount(), 0);
        assertEquals("Incorrect rate", 6.9, response.getPercentageRate(), maxDelta(formatProperties.getRateDecimalPlaces()));
        assertEquals("Incorrect monthly repayment", 30.83, response.getMonthlyRepayment(), maxDelta(formatProperties.getRepaymentDecimalPlaces()));
        assertEquals("Incorrect monthly repayment", 1110.09, response.getTotalRepayment(), maxDelta(formatProperties.getRepaymentDecimalPlaces()));
    }

    @Test
    public void testProcessLendingRequest_shouldReturnOkResponse_withoutInteresetInvolved() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "market_zero_rate_ok.csv";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(OK, responseEntity.getStatus());
        assertNull(responseEntity.getException());
        assertNotNull(response);

        assertEquals("Incorrect loan amount", loanAmount, response.getRequestedAmount(), 0);
        assertEquals("Incorrect rate", 0, response.getPercentageRate(), maxDelta(formatProperties.getRateDecimalPlaces()));
        assertEquals("Incorrect monthly repayment", 27.78, response.getMonthlyRepayment(), maxDelta(formatProperties.getRepaymentDecimalPlaces()));
        assertEquals("Incorrect monthly repayment", 1000, response.getTotalRepayment(), maxDelta(formatProperties.getRepaymentDecimalPlaces()));
    }

    @Test
    public void testProcessLendingRequest_shouldReturnExceptionalResponse_whenFileNotFound() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "error.file";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(USER_EXCEPTION, responseEntity.getStatus());
        assertNotNull(responseEntity.getException());
        assertNull(response);

        assertTrue(responseEntity.getException() instanceof UserException);
        assertEquals(FILE_NOT_FOUND_EM, responseEntity.getException().getMessage());
    }

    @Test
    public void testProcessLendingRequest_shouldReturnExceptionalResponse_whenEmptyRowExist() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "market_empty_row_error.csv";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(USER_EXCEPTION, responseEntity.getStatus());
        assertNotNull(responseEntity.getException());
        assertNull(response);

        assertTrue(responseEntity.getException() instanceof UserException);
        assertEquals(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, responseEntity.getException().getMessage());
    }


    @Test
    public void testProcessLendingRequest_shouldReturnExceptionalResponse_whenHeaderInvalid() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "market_invalid_header.csv";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(USER_EXCEPTION, responseEntity.getStatus());
        assertNotNull(responseEntity.getException());
        assertNull(response);

        assertTrue(responseEntity.getException() instanceof UserException);
        assertEquals(MessageFormat.format(FILE_INCORRECT_HEADER_NAME_EM, Arrays.toString(new String[]{"Something", "Rate", "Available"})),
                responseEntity.getException().getMessage());
    }

    @Test
    public void testProcessLendingRequest_shouldReturnExceptionalResponse_whenOneOfValuesInvalid() throws Exception {
        Double loanAmount = 1000D;
        String marketFileName = inputFilesTestDir + "market_invalid_value.csv";

        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(marketFileName, loanAmount.toString());

        LendingResponse response = responseEntity.getEntity();

        assertEquals(USER_EXCEPTION, responseEntity.getStatus());
        assertNotNull(responseEntity.getException());
        assertNull(response);

        assertTrue(responseEntity.getException() instanceof UserException);
        assertEquals(MessageFormat.format(FILE_NEGATIVE_RATE_EM, "Mary"), responseEntity.getException().getMessage());
    }

    private double maxDelta(int decimalPlaces) {
        return 1 / (Math.pow(10, decimalPlaces));
    }
}
