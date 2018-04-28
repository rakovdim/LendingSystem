package com.drakov.lending.controller.console;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.service.LendingService;
import com.drakov.lending.service.ModelService;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

import static com.drakov.lending.constants.LendingConstants.ARGS_INCORRECT_ARGUMENTS_COUNT_EM;
import static com.drakov.lending.constants.LendingConstants.ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM;
import static com.drakov.lending.constants.LendingConstants.ARGS_LOAN_AMOUNT_IS_NOT_NUMERIC_EM;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CommonLendingControllerTest {

    private CommonLendingController controller;

    @Mock
    private ModelService modelService;
    @Mock
    private LendingService lendingService;
    @Mock
    private LoanProperties loanProperties;

    @Before
    public void setUp() {
        CommonLendingController controller =
                new CommonLendingController(modelService, lendingService, loanProperties);

        this.controller = spy(controller);
        doNothing().when(this.controller).logException(any());
    }

    @Test
    public void testProcessLendingRequest_shouldReturnExceptionResponseEntity_ifUserExceptionThrown() throws UserException {

        UserException e = new UserException();
        when(lendingService.calculateLoan(anyDouble())).thenThrow(e);
        mockLoanProperties(1000, 15000, 100);

        ResponseEntity<LendingResponse> responseEntity = controller.processLendingRequest("market.csv", "1000");

        assertEquals(ResponseEntity.STATUS.USER_EXCEPTION, responseEntity.getStatus());
        assertNull(responseEntity.getEntity());
        assertSame(e, responseEntity.getException());
    }

    @Test
    public void testRun_shouldReturnErrorResponseEntity_ifRuntimeExceptionThrown() throws UserException {

        RuntimeException e = new RuntimeException();
        when(lendingService.calculateLoan(anyDouble())).thenThrow(e);
        mockLoanProperties(1000, 15000, 100);

        ResponseEntity<LendingResponse> responseEntity = controller.processLendingRequest("market.csv", "1000");

        assertEquals(ResponseEntity.STATUS.INTERNAL_ERROR, responseEntity.getStatus());
        assertNull(responseEntity.getEntity());
        assertSame(e, responseEntity.getException());
    }

    @Test
    public void testRun_shouldReturnOkResponseEntity_ifNoErrorsOccurred() throws UserException {

        String fileName = "market.csv";
        Double loanAmount = 1000D;

        LendingResponse response = mock(LendingResponse.class);
        mockLoanProperties(1000, 15000, 100);

        when(lendingService.calculateLoan(loanAmount)).thenReturn(response);

        ResponseEntity<LendingResponse> responseEntity = controller.processLendingRequest(fileName, loanAmount.toString());

        assertEquals(ResponseEntity.STATUS.OK, responseEntity.getStatus());
        assertNull(responseEntity.getException());
        assertSame(response, responseEntity.getEntity());

        verify(lendingService, times(1)).calculateLoan(loanAmount);
        verify(modelService, times(1)).uploadModelDataFile(fileName);
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenArgsAreNull() throws UserException {

        ResponseEntity entity = controller.processLendingRequest();

        assertUserExceptionResponseEntity(ARGS_INCORRECT_ARGUMENTS_COUNT_EM, entity);
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenOneArgIsPassed() throws UserException {

        ResponseEntity entity = controller.processLendingRequest("market_file.csv");

        assertUserExceptionResponseEntity(ARGS_INCORRECT_ARGUMENTS_COUNT_EM, entity);

    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenThreeArgsArePassed() throws UserException {

        ResponseEntity entity = controller.processLendingRequest("market_file.csv", "4200", "100");

        assertUserExceptionResponseEntity(ARGS_INCORRECT_ARGUMENTS_COUNT_EM, entity);

    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotNumeric() throws UserException {

        ResponseEntity entity = controller.processLendingRequest("market_file.csv", "test");

        assertUserExceptionResponseEntity(ARGS_LOAN_AMOUNT_IS_NOT_NUMERIC_EM, entity);

    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsLessThanMinimum() throws UserException {

        mockLoanProperties(1000, 15000, 100);

        ResponseEntity entity = controller.processLendingRequest("market_file.csv", "900");

        assertUserExceptionResponseEntity(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100), entity);

    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsMoreThanMaximum() throws UserException {

        mockLoanProperties(1000, 15000, 100);

        ResponseEntity entity = controller.processLendingRequest("market_file.csv", "15100");

        assertUserExceptionResponseEntity(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100), entity);

    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotMultipleOf100() throws UserException {

        mockLoanProperties(1000, 15000, 100);

        ResponseEntity entity = controller.processLendingRequest("market_file.csv", "4004");
        assertUserExceptionResponseEntity(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100), entity);

    }

    private void mockLoanProperties(double min, double max, int multiply) {
        when(loanProperties.getMaxValue()).thenReturn(max);
        when(loanProperties.getMinValue()).thenReturn(min);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(multiply);
    }

    private static void assertUserExceptionResponseEntity(String expectedMessage, ResponseEntity responseEntity) {
        assertNull(responseEntity.getEntity());
        assertNotNull(responseEntity.getException());
        assertTrue(responseEntity.getException() instanceof UserException);
        assertEquals(ResponseEntity.STATUS.USER_EXCEPTION, responseEntity.getStatus());
        assertEquals(expectedMessage, responseEntity.getException().getMessage());
    }

    private static void assertInternalErrorResponseEntity(ResponseEntity responseEntity) {
        assertNull(responseEntity.getEntity());
        assertNotNull(responseEntity.getException());
        assertEquals(ResponseEntity.STATUS.INTERNAL_ERROR, responseEntity.getStatus());
    }
}