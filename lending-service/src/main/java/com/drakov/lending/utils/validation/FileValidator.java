package com.drakov.lending.utils.validation;

import com.drakov.lending.exceptions.UserException;
import org.springframework.util.StringUtils;

import java.util.Arrays;

import static com.drakov.lending.constants.LendingConstants.*;

//todo logs
public class FileValidator {

    public static void validateHeaders(String[] headerColumns) throws UserException {

        validateRowNotEmpty(headerColumns);

        if (headerColumns.length != 3)
            throw new UserException(FILE_INCORRECT_HEADERS_COUNT_EM, Arrays.toString(headerColumns));

        if (!FILE_LENDER_HEADER_NAME.equalsIgnoreCase(headerColumns[0]) ||
                !FILE_RATE_HEADER_NAME.equalsIgnoreCase(headerColumns[1]) ||
                !FILE_AVAILABLE_HEADER_NAME.equalsIgnoreCase(headerColumns[2]))

            throw new UserException(FILE_INCORRECT_HEADER_NAME_EM, Arrays.toString(headerColumns));
    }

    public static void validateValues(String[] offerRow) throws UserException {

        validateRowNotEmpty(offerRow);

        if (offerRow.length != 3)
            throw new UserException(FILE_INCORRECT_OFFER_VALUES_COUNT_EM, Arrays.toString(offerRow));

        if (StringUtils.isEmpty(offerRow[0]) || StringUtils.isEmpty(offerRow[1]) || StringUtils.isEmpty(offerRow[2]))
            throw new UserException(FILE_INCORRECT_OFFER_VALUES_EM, Arrays.toString(offerRow));

        validateRate(offerRow);
        validateAvailable(offerRow);
    }

    private static void validateRowNotEmpty(String[] row) throws UserException {
        if (row == null || row.length == 0 ||
                (row.length == 1 && StringUtils.isEmpty(row[0])))
            throw new UserException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM);
    }

    private static void validateRate(String[] row) throws UserException {
        double rate = validatePositiveDoubleValue(row, row[1]);

        if (rate < 0)
            throw new UserException(FILE_NEGATIVE_RATE_EM, row[1], Arrays.toString(row));
    }

    private static void validateAvailable(String[] row) throws UserException {
        Double available = validatePositiveDoubleValue(row, row[2]);

        if (available < 0)
            throw new UserException(FILE_NEGATIVE_AVAILABLE_EM, row[2], Arrays.toString(row));
    }

    private static double validatePositiveDoubleValue(String[] row, String value) throws UserException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new UserException(FILE_VALUE_IS_NOT_NUMERIC_EM, value, Arrays.toString(row));
        }
    }
}
