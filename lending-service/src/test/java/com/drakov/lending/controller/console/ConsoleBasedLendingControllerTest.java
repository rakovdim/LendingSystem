package com.drakov.lending.controller.console;

import com.drakov.lending.controller.ControllerRequestProcessor;
import com.drakov.lending.controller.console.format.ResponseFormatter;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ConsoleBasedLendingControllerTest {

    private ConsoleBasedLendingController controller;

    @Mock
    private ResponseFormatter formatter;
    @Mock
    private ControllerRequestProcessor requestProcessor;

    @Before
    public void setUp() {
        controller = new ConsoleBasedLendingController(requestProcessor, formatter);
    }

    @Test
    public void testRun_shouldHandleUserException_ifUserExceptionThrown() throws UserException {

        UserException e = new UserException();

        when(requestProcessor.uploadFileAndCalculateLoan(any())).thenThrow(e);

        ConsoleBasedLendingController controllerSpy = spy(controller);
        controllerSpy.run();

        verify(controllerSpy, times(1)).handleUserException(e);
    }

    @Test
    public void testRun_shouldHandleInternalException_ifRuntimeExceptionThrown() throws UserException {

        RuntimeException e = new RuntimeException();

        when(requestProcessor.uploadFileAndCalculateLoan(any())).thenThrow(e);

        ConsoleBasedLendingController controllerSpy = spy(controller);
        controllerSpy.run();

        verify(controllerSpy, times(1)).handleInternalException(e);
    }

    @Test
    public void testRun_shouldCallShowResults_ifNoErrorsOccurred() throws UserException {

        LendingResponse response = spy(LendingResponse.class);
        String[] args = {"market_file.csv", "300"};

        when(requestProcessor.uploadFileAndCalculateLoan(args)).thenReturn(response);

        controller.run(args);

        verify(requestProcessor, times(1)).uploadFileAndCalculateLoan(args);
        verify(formatter, times(1)).format(response);
    }
}