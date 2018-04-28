package com.drakov.lending.controller.console;

import com.drakov.lending.dto.LendingResponse;

/**
 * Created by rakov on 28.04.2018.
 */
public interface LendingController {
    public ResponseEntity<LendingResponse> processLendingRequest(String... args);
}
