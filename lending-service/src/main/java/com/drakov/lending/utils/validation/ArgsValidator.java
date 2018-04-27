package com.drakov.lending.utils.validation;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.exceptions.UserException;

import static com.drakov.lending.constants.LendingConstants.*;

public class ArgsValidator {

    public static void validateInputArgs(LoanProperties props, String... args) throws UserException {

        validateArgsCount(args);

        validateLoanAmountArg(props, args[1]);
    }

    private static void validateArgsCount(String... args) throws UserException {
        if (args.length != 2)
            throw new UserException(INCORRECT_ARGUMENTS_COUNT_EM);
    }

    private static void validateLoanAmountArg(LoanProperties props, String loanAmountArg) throws UserException {
        try {
            double loanAmount = Double.parseDouble(loanAmountArg);

            validateLoanAmount(props, loanAmount);

        } catch (NumberFormatException e) {
            throw new UserException(LOAN_AMOUNT_INCORRECT_FORMAT_ERROR_MESSAGE, e);
        }
    }

    private static void validateLoanAmount(LoanProperties props, double loanAmount) throws UserException {
        if (loanAmount <= 0)
            throw new UserException(LOAN_AMOUNT_NOT_POSITIVE_EM, loanAmount);

        if (loanAmount < props.getMinValue()
                || loanAmount > props.getMaxValue()
                || loanAmount % props.getLoanAmountMultiple() != 0)

            throw new UserException(LOAN_AMOUNT_INCORRECT_VALUE_EM, props.getMinValue(),
                    props.getMaxValue(), props.getLoanAmountMultiple());

    }


}
