package com.drakov.lending.view;

import com.drakov.lending.constants.LendingConstants;
import com.drakov.lending.controller.console.CommonLendingController;
import com.drakov.lending.controller.console.ResponseEntity;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.InternalProcessingException;
import com.drakov.lending.view.format.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by rakov on 28.04.2018.
 */
@Component
@Profile("production")
public class SimpleConsoleView implements CommandLineRunner {

    private CommonLendingController lendingController;
    private ResponseFormatter responseFormatter;

    @Autowired
    public SimpleConsoleView(CommonLendingController lendingController, ResponseFormatter responseFormatter) {
        this.lendingController = lendingController;
        this.responseFormatter = responseFormatter;
    }

    @Override
    public void run(String... args) throws Exception {
        ResponseEntity<LendingResponse> responseEntity = lendingController.processLendingRequest(args);

        showResponse(responseEntity);
    }

    protected void showResponse(ResponseEntity<LendingResponse> responseEntity) {
        switch (responseEntity.getStatus()) {
            case OK:
                System.out.println(responseFormatter.format(responseEntity.getEntity()));
            case INTERNAL_ERROR:
                System.out.println(LendingConstants.INTERNAL_EXCEPTION_OCCURRED_EM);
            case USER_EXCEPTION:
                System.out.println(responseEntity.getException().getMessage());
            default:
                throw new InternalProcessingException("Unknown response status. Response Entity: " + responseEntity);
        }
    }
}
