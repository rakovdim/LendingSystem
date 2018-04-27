package com.drakov.lending.controller;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.controller.ControllerRequestProcessor;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.service.LendingService;
import com.drakov.lending.service.ModelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
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

    @Before
    public void setUp() throws Exception {
        requestProcessor = new ControllerRequestProcessor(modelService, lendingService, loanProperties);

        spyRequestProcessor = spy(requestProcessor);
        doNothing().when(spyRequestProcessor).processMarketFile(any());
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenArgsAreNull() throws UserException {
        spyRequestProcessor.uploadFileAndCalculateLoan();

        fail("User Exception wasn't thrown but args are null");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenOneArgIsPassed() throws UserException {

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv");

        fail("User Exception wasn't thrown but only one args was passed");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenThreeArgsArePassed() throws UserException {

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "4200", "100");

        fail("User Exception wasn't thrown but three args were passed");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotNumeric() throws UserException {

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "test");

        fail("User Exception wasn't thrown but loan amount is not a numeric");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsLessThanMinimum() throws UserException {

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "500");

        fail("User Exception wasn't thrown but loan amount is less than minimum");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsMoreThanMaximum() throws UserException {

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "30000");

        fail("User Exception wasn't thrown but loan amount is more than maximum");
    }

    @Test(expected = UserException.class)
    public void testUploadFileAndCalculateLoan_shouldThrowUserException_whenLoanAmountIsNotMultipleOf() throws UserException {

        when(loanProperties.getMinValue()).thenReturn(1000d);
        when(loanProperties.getMaxValue()).thenReturn(15000d);
        when(loanProperties.getLoanAmountMultiple()).thenReturn(100);

        spyRequestProcessor.uploadFileAndCalculateLoan("market_file.csv", "4004");

        fail("User Exception wasn't thrown but loan amount is not a multiple of 100");
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
}