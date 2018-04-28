package com.drakov.lending.utils.validation;

import com.drakov.lending.exceptions.UserException;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.function.Function;

import static com.drakov.lending.constants.LendingConstants.*;

//todo logs
public class FileValidator {

    public static void validateHeaders(String[] headerColumns) throws UserException {

        validateRowNotEmpty(headerColumns);

        if (headerColumns.length != 3)
            throw new UserException(FILE_INCORRECT_HEADERS_COUNT_EM);

        if (!FILE_LENDER_HEADER_NAME.equalsIgnoreCase(headerColumns[0]) ||
                !FILE_RATE_HEADER_NAME.equalsIgnoreCase(headerColumns[1]) ||
                !FILE_AVAILABLE_HEADER_NAME.equalsIgnoreCase(headerColumns[2]))

            throw new UserException(FILE_INCORRECT_HEADER_NAME_EM);
    }

    public static void validateValues(String[] lenderRow) throws UserException {

        validateRowNotEmpty(lenderRow);

        if (lenderRow.length != 3)
            throw new UserException(FILE_INCORRECT_LENDER_VALUES_COUNT_EM);

        if (StringUtils.isEmpty(lenderRow[0]) || StringUtils.isEmpty(lenderRow[1]) || StringUtils.isEmpty(lenderRow[2]))
            throw new UserException(FILE_INCORRECT_LENDER_VALUES_EM, Arrays.toString(lenderRow));

        double rate = extractValue(lenderRow[0], lenderRow[1], Double::parseDouble);
        double available = extractValue(lenderRow[0], lenderRow[2], Double::parseDouble);

        if (rate < 0)
            throw new UserException(FILE_NEGATIVE_RATE_EM, lenderRow[0]);

        if (available < 0)
            throw new UserException(FILE_NEGATIVE_AVAILABLE_EM, lenderRow[0]);
    }

    private static void validateRowNotEmpty(String[] row) throws UserException {
        if (row == null || row.length == 0 ||
                (row.length == 1 && StringUtils.isEmpty(row[0])))
            throw new UserException(FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM);
    }

    private static <T> T extractValue(String lender, String value, Function<String, T> extractor) throws UserException {
        try {
            return extractor.apply(value);
        } catch (NumberFormatException e) {
            throw new UserException(FILE_VALUE_IS_NOT_NUMERIC_EM, value, lender);
        }
    }
}
