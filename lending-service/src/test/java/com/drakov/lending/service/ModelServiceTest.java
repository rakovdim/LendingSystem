package com.drakov.lending.service;

import com.drakov.lending.TestUtils;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Offer;
import com.drakov.lending.repository.OfferRepository;
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

import static com.drakov.lending.constants.LendingConstants.NO_OFFERS_FOUND_DURING_STREAM_PROCESSING_EM;
import static org.mockito.Mockito.*;

/**
 * Created by dima on 28.04.18.
 */
@RunWith(SpringRunner.class)
public class ModelServiceTest {

    private ModelService modelService;
    @Mock
    private OfferRepository repository;
    @Mock
    private ModelDataStreamProcessor streamProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.modelService = new ModelService(streamProcessor, repository);
    }

    @Test
    public void testUploadModelDataStream_shouldThrowException_IfNoOffersWereUploaded() throws Exception {

        thrown.expect(UserException.class);
        thrown.expectMessage(NO_OFFERS_FOUND_DURING_STREAM_PROCESSING_EM);

        Reader reader = mock(Reader.class);

        when(streamProcessor.uploadOffers(reader)).thenReturn(Collections.emptyList());

        modelService.uploadOffersStream(reader);
    }

    @Test
    public void testUploadModelDataStream_shouldCallRepository_ForEachUploadedOffer() throws Exception {

        Reader reader = mock(Reader.class);

        Offer offer1 = TestUtils.mockEmptyOffer();
        Offer offer2 = TestUtils.mockEmptyOffer();
        Offer offer3 = TestUtils.mockEmptyOffer();

        when(streamProcessor.uploadOffers(reader)).thenReturn(Arrays.asList(offer1, offer2, offer3));

        modelService.uploadOffersStream(reader);

        verify(repository, times(1)).save(offer1);
        verify(repository, times(1)).save(offer2);
        verify(repository, times(1)).save(offer3);
    }
}