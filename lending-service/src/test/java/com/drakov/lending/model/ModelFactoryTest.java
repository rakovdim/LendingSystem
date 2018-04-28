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
    public void testCreateLender_shouldReturnLenderWithId_withAllFieldsSet() {

        Long id = 1L;
        String name = "James";
        double rate = 0.12f;
        double available = 1200;

        when(idGenerator.generateId()).thenReturn(id);

        Lender lender = modelFactory.createLender(name, rate, available);

        assertEquals("Incorrect id was generated", id, lender.getId(), 0);
        assertEquals("Incorrect name was set", name, lender.getName());
        assertEquals("Incorrect rate was set", rate, lender.getRate(), 0);
        assertEquals("Incorrect available was set", available, lender.getAvailable(), 0);
    }
}