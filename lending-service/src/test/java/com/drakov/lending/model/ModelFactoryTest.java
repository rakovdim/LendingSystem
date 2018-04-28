package com.drakov.lending.model;

import com.drakov.lending.utils.id.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by dima on 27.04.18.
 */
@RunWith(SpringRunner.class)
public class ModelFactoryTest {
    @Mock
    private IdGenerator idGenerator;

    private ModelFactory modelFactory;

    @Before
    public void setUp() throws Exception {
        this.modelFactory = new ModelFactory(idGenerator);
    }

    @Test
    public void testCreateOffer_shouldReturnOfferWithId_withAllFieldsSet() {

        Long id = 1L;
        String lender = "James";
        double rate = 0.12f;
        double available = 1200;

        when(idGenerator.generateId()).thenReturn(id);

        Offer offer = modelFactory.createOffer(lender, rate, available);

        assertEquals("Incorrect id was generated", id, offer.getId(), 0);
        assertEquals("Incorrect lender was set", lender, offer.getLender());
        assertEquals("Incorrect rate was set", rate, offer.getRate(), 0);
        assertEquals("Incorrect available was set", available, offer.getAvailable(), 0);
    }
}