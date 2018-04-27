package com.drakov.lending.controller;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.service.LendingService;
import com.drakov.lending.service.ModelService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

import static com.drakov.lending.constants.LendingConstants.*;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by rakov on 27.04.2018.
 */
@RunWith(SpringRunner.class)
public class ControllerRequestProcessorTest {

    @Mock
    private LoanProperties loanProperties;
    @Mock
    private ModelService modelService;
    @Mock
    private LendingService lendingService;

    private ControllerRequestProcessor requestProcessor;
    private ControllerRequestProcessor spyRequestProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        requestProcessor = new ControllerRequestProcessor(modelService, lendingService, loanProperties);

        spyRequestProcessor = spy(requestProcessor);
        doNothing().when(spyRequestProcessor).processMarketFile(any());
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenArgsAreNull() throws UserException {

        expectUserException(ARGS_INCORRECT_ARGUMENTS_COUNT_EM);

        spyRequestProcessor.uploadFileAndCalculateLoan();
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldReturnResponse_whenArgsAreValid() throws UserException {

        Double loanAmount = 4000d;
        LendingResponse response = LendingResponse.newOne();
        String marketFileName = "market_file.csv";

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        when(lendingService.calculateLoan(loanAmount)).thenReturn(response);

        LendingResponse actualResponse = spyRequestProcessor.uploadFileAndCalculateLoan(marketFileName, loanAmount.toString());

        verify(lendingService, times(1)).calculateLoan(loanAmount);
        verify(spyRequestProcessor, times(1)).processMarketFile(marketFileName);
        assertSame("Actual and expected responses aren't equal", response, actualResponse);
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenOneArgIsPassed() throws UserException {

        expectUserException(ARGS_INCORRECT_ARGUMENTS_COUNT_EM);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv");
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenThreeArgsArePassed() throws UserException {

        expectUserException(ARGS_INCORRECT_ARGUMENTS_COUNT_EM);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "4200", "100");
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotNumeric() throws UserException {

        expectUserException(ARGS_LOAN_AMOUNT_IS_NOT_NUMERIC_EM);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "test");
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsLessThanMinimum() throws UserException {

        expectUserException(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100));

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "900");
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsMoreThanMaximum() throws UserException {

        expectUserException(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100));

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "15100");
    }

    @Test
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotMultipleOf100() throws UserException {

        expectUserException(MessageFormat.format(ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM, 1000, 15000, 100));

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "4004");
    }

    private void expectUserException(String message) {
        thrown.expect(UserException.class);
        thrown.expectMessage(message);
    }
}