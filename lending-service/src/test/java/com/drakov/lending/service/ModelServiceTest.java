package com.drakov.lending.service;

import com.drakov.lending.TestUtils;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.repository.LenderRepository;
import com.drakov.lending.service.file.ModelDataStreamProcessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;

import static com.drakov.lending.constants.LendingConstants.NO_LENDERS_FOUND_DURING_STREAM_PROCESSING_EM;
import static org.mockito.Mockito.*;

/**
 * Created by dima on 28.04.18.
 */
@RunWith(SpringRunner.class)
public class ModelServiceTest {

    private ModelService modelService;
    @Mock
    private LenderRepository repository;
    @Mock
    private ModelDataStreamProcessor streamProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.modelService = new ModelService(streamProcessor, repository);
    }

    @Test
    public void testUploadModelDataStream_shouldThrowException_IfNoLendersWereUploaded() throws Exception {

        thrown.expect(UserException.class);
        thrown.expectMessage(NO_LENDERS_FOUND_DURING_STREAM_PROCESSING_EM);

        Reader reader = mock(Reader.class);

        when(streamProcessor.uploadStreamData(reader)).thenReturn(Collections.emptyList());

        modelService.uploadModelDataStream(reader);
    }

    @Test
    public void testUploadModelDataStream_shouldCallRepository_ForEachUploadedLender() throws Exception {

        Reader reader = mock(Reader.class);

        Lender lender1 = TestUtils.mockEmptyLender();
        Lender lender2 = TestUtils.mockEmptyLender();
        Lender lender3 = TestUtils.mockEmptyLender();

        when(streamProcessor.uploadStreamData(reader)).thenReturn(Arrays.asList(lender1, lender2, lender3));

        modelService.uploadModelDataStream(reader);

        verify(repository, times(1)).save(lender1);
        verify(repository, times(1)).save(lender2);
        verify(repository, times(1)).save(lender3);
    }
}