package com.drakov.lending.constants;

public interface LendingConstants {

    String NO_LENDER_WAS_FOUND_DURING_LOAN_CALC_EM = "No lender was found. Perhaps there is no lender with requested budget";
    String INTERNAL_EXCEPTION_OCCURRED_EM = "Internal error occurred. Please contact system administrator";

    //Args validation error messages
    String ARGS_INCORRECT_ARGUMENTS_COUNT_EM = "Incorrect input parameters count. Please specify market_file and loan_amount";
    String ARGS_LOAN_AMOUNT_BASE_EM = "Incorrect loan amount: ";
    String ARGS_LOAN_AMOUNT_IS_NOT_NUMERIC_EM = ARGS_LOAN_AMOUNT_BASE_EM + "it must be a numeric";
    String ARGS_LOAN_AMOUNT_INCORRECT_VALUE_EM = ARGS_LOAN_AMOUNT_BASE_EM + "it must be between {0} and {1} and it must be a multiple of {2}";
    String ARGS_LOAN_AMOUNT_NOT_POSITIVE_EM = ARGS_LOAN_AMOUNT_BASE_EM + "incorrect loan amount: {0}. It must be positive";

    //File processing error messages
    String FILE_BASE_PROCESSING_EM = "Error during processing csv file: ";
    String FILE_NOT_FOUND_EM = FILE_BASE_PROCESSING_EM + "csv file not found";

    String FILE_INCORRECT_HEADERS_COUNT_EM = FILE_BASE_PROCESSING_EM + "incorrect header columns count. There should be three: Lender, Rate and Available (commas forbidden)";
    String FILE_INCORRECT_HEADER_NAME_EM = FILE_BASE_PROCESSING_EM + "incorrect header name. Next strictly ordered headers are expected: Lender,Rate,Available";

    String FILE_EMPTY_ROWS_ARE_NOT_SUPPORTED_EM = FILE_BASE_PROCESSING_EM + " empty rows are not supported";
    String FILE_INCORRECT_LENDER_VALUES_COUNT_EM = FILE_BASE_PROCESSING_EM + "incorrect lender values count. There should be 3 in a row: Lender value, rate value and available value. Commas forbidden";
    String FILE_INCORRECT_LENDER_VALUES_EM = FILE_BASE_PROCESSING_EM + "one of lender values is empty in a row: {0}. Lender, Rate and Available should be specified. Nulls are forbidden ";

    String FILE_NEGATIVE_RATE_EM = FILE_BASE_PROCESSING_EM + "rate value of lender {0} can't be negative";
    String FILE_NEGATIVE_AVAILABLE_EM = FILE_BASE_PROCESSING_EM + "available value of lender {0} can't be negative";

    String FILE_VALUE_IS_NOT_NUMERIC_EM = FILE_BASE_PROCESSING_EM + "value {0} is not a valid numeric (note: commas in value are forbidden) Lender: {1}";
    String NO_LENDERS_FOUND_DURING_STREAM_PROCESSING_EM = "No lenders were found during model data stream processing";


    String FILE_LENDER_HEADER_NAME = "Lender";
    String FILE_RATE_HEADER_NAME = "Rate";
    String FILE_AVAILABLE_HEADER_NAME = "Available";


    String EURO = "\u20ac";
    String REQUEST_ID_MDC_PARAM = "request_id";
}
