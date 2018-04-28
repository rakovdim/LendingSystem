package com.drakov.lending.service.file;

import com.drakov.lending.TestUtils;
import com.drakov.lending.clients.csv.CsvClient;
import com.drakov.lending.clients.csv.CsvClientFactory;
import com.drakov.lending.exceptions.UserException;
import com.drakov.lending.model.Lender;
import com.drakov.lending.model.ModelFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static com.drakov.lending.constants.LendingConstants.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class CSVModelDataStreamProcessorTest {
    @Mock
    private ModelFactory modelFactory;
    @Mock
    private CsvClientFactory csvClientFactory;
    @Mock
    private Reader reader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CSVModelDataStreamProcessor streamProcessor;

    @Before
    public void setUp() throws Exception {
        this.streamProcessor = new CSVModelDataStreamProcessor(modelFactory, csvClientFactory);
    }

    @Test
    public void testUploadStream_shouldReturnFourLenders_whenFourLenderValuesAreInStream() throws Exception {

        String[] row1 = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] row2 = {"Bob", "12.2", "1000"};
        String[] row3 = {"Alex", "0", "3333"};
        String[] row4 = {"John", "11.22", "0"};
        String[] row5 = {"James", "12.2", "10000"};

        CsvClient client = new CsvClientStub(new String[][]{row1, row2, row3, row4, row5});
        when(csvClientFactory.create(reader)).thenReturn(client);

        streamProcessor.uploadStreamData(reader);

        verify(csvClientFactory, times(1)).create(reader);

        verify(modelFactory, times(1)).createLender("Bob", 12.2, 1000);
        verify(modelFactory, times(1)).createLender("Alex", 0f, 3333);
        verify(modelFactory, times(1)).createLender("John", 11.22, 0);
        verify(modelFactory, times(1)).createLender("James", 12.2, 10000);
    }

    @Test
    public void testUploadStream_shouldReturnOneLender_whenModelFactoryCreateIt() throws Exception {
        String[] row1 = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] row2 = {"Bob", "12.2", "1000"};

        Lender oneLender = TestUtils.mockEmptyLender();
        CsvClient client = new CsvClientStub(new String[][]{row1, row2});
        when(csvClientFactory.create(reader)).thenReturn(client);
        when(modelFactory.createLender("Bob", 12.2, 1000)).thenReturn(oneLender);

        List<Lender> lenderList = streamProcessor.uploadStreamData(reader);

        assertTrue(lenderList.size() == 1);
        assertSame(oneLender, lenderList.get(0));
    }

    @Test
    public void testUploadStreamData_shouldReturnEmptyLenders_whenNoLendersInStream() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        CsvClient client = new CsvClientStub(new String[][]{firstRow});
        when(csvClientFactory.create(reader)).thenReturn(client);

        streamProcessor.uploadStreamData(reader);

        verify(csvClientFactory, times(1)).create(reader);
        verify(modelFactory, never()).createLender(anyString(), anyDouble(), anyDouble());
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenNoRowsFoundInStream() throws Exception {
        testIncorrectFileDataException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, new String[][]{});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenEmptyStringInStream() throws Exception {
        testIncorrectFileDataException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, new String[][]{{""}});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenNullStringInStream() throws Exception {
        testIncorrectFileDataException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, new String[][]{{null}});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenOneHeaderFound() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADERS_COUNT_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenTwoHeadersFound() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADERS_COUNT_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenThirdHeaderInvalid() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, "test"};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADER_NAME_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenSecondHeaderInvalid() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, "test", FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADER_NAME_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenFirstHeaderInvalid() throws Exception {

        String[] firstRow = {"test", FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADER_NAME_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenInvalidHeadersOrder() throws Exception {

        String[] firstRow = {FILE_RATE_HEADER_NAME, FILE_LENDER_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "1000"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_HEADER_NAME_EM, firstRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenTwoLenderValuesInStream() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_LENDER_VALUES_COUNT_EM, secondRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenFourLenderValuesInStream() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "11000", "111"};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_LENDER_VALUES_COUNT_EM, secondRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenTwoLenderValuesWithEmpty() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", ""};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_LENDER_VALUES_EM, secondRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenTwoLenderValuesWithNull() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", null};

        testIncorrectFileDataException(formatOneArr(FILE_INCORRECT_LENDER_VALUES_EM, secondRow), new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenEmptyValueRowExist() throws Exception {

        String[] row1 = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] row2 = {"Bob", "12.2", "100"};
        String[] row3 = {""};

        testIncorrectFileDataException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, new String[][]{row1, row2, row3});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenNullValueRowExist() throws Exception {

        String[] row1 = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] row2 = {null};
        String[] row3 = {"Bob", "12.2", "100"};

        testIncorrectFileDataException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM, new String[][]{row1, row2, row3});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenRateIsNotNumeric() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "test", "1000"};

        testIncorrectFileDataException(format(FILE_VALUE_IS_NOT_NUMERIC_EM, "test", Arrays.toString(secondRow)),
                new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenAvailableIsNotNumeric() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "test"};

        testIncorrectFileDataException(format(FILE_VALUE_IS_NOT_NUMERIC_EM, "test", Arrays.toString(secondRow)),
                new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenAvailableIsNegative() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "12.2", "-1000"};

        testIncorrectFileDataException(format(FILE_NEGATIVE_AVAILABLE_EM, "-1000", Arrays.toString(secondRow)),
                new String[][]{firstRow, secondRow});
    }

    @Test
    public void testUploadStreamData_shouldThrowUserException_whenRateIsNegative() throws Exception {

        String[] firstRow = {FILE_LENDER_HEADER_NAME, FILE_RATE_HEADER_NAME, FILE_AVAILABLE_HEADER_NAME};
        String[] secondRow = {"Bob", "-12.2", "1000"};

        testIncorrectFileDataException(format(FILE_NEGATIVE_RATE_EM, "-12.2", Arrays.toString(secondRow)),
                new String[][]{firstRow, secondRow});
    }


    private void testIncorrectFileDataException(String expectedMessage, String[][] data) throws Exception {
        expectUserExceptionWithMessage(expectedMessage);

        CsvClient client = new CsvClientStub(data);
        when(csvClientFactory.create(reader)).thenReturn(client);

        streamProcessor.uploadStreamData(reader);
    }

    private void expectUserExceptionWithMessage(String message) {
        thrown.expect(UserException.class);
        thrown.expectMessage(message);
    }

    private CsvClient mockEmptyCsvClient() throws IOException {
        return mockOneRowCsvClient();
    }

    private CsvClient mockOneRowCsvClient(String... row) throws IOException {
        CsvClient csvClient = mock(CsvClient.class);
        when(csvClient.readRow()).thenReturn(row);
        return csvClient;
    }

    private static String formatOneArr(String pattern, String[] array) {
        return format(pattern, Arrays.toString(array));
    }

    private static String format(String pattern, Object... args) {
        return MessageFormat.format(pattern, args);
    }

}