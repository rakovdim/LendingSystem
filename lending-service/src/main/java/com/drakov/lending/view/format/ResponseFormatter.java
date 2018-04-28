package com.drakov.lending.view.format;

import com.drakov.lending.dto.LendingResponse;

/**
 * Created by dima on 24.04.18.
 */
public interface ResponseFormatter {
    public String format(LendingResponse response);
}
