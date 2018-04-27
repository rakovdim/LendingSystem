package com.drakov.lending.controller.console.format;

import com.drakov.lending.config.FormatProperties;
import com.drakov.lending.dto.LendingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.drakov.lending.constants.LendingConstants.EURO;

/**
 * Created by dima on 24.04.18.
 */
@Component
public class SimpleTextResponseFormatter implements ResponseFormatter {

    private FormatProperties props;

    @Autowired
    public SimpleTextResponseFormatter(FormatProperties props) {
        this.props = props;
    }

    @Override
    public String format(LendingResponse response) {

        return "Requested amount: " + EURO + formatLoanAmount(response.getRequestedAmount()) + "\n" +
                "Rate: " + formatRate(response.getPercentageRate()) + "%" + "\n" +
                "Monthly repayment: " + EURO + formatRepayment(response.getMonthlyRepayment()) + "\n" +
                "Total repayment: " + EURO + formatRepayment(response.getTotalRepayment());
    }

    private String formatLoanAmount(double loanAmount) {
        return String.format("%.0f", loanAmount);
    }

    private String formatRate(double rate) {
        return formatDecimal(rate, props.getRateDecimalPlaces());
    }

    private String formatRepayment(double repayment) {
        return formatDecimal(repayment, props.getRepaymentDecimalPlaces());
    }

    private static String formatDecimal(double value, int decimalPlaces) {
        return new BigDecimal(value).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toString();
    }

}
