package com.drakov.lending.controller.console;

import com.drakov.lending.config.LoanProperties;
import com.drakov.lending.service.LendingService;
import com.drakov.lending.service.ModelService;
import com.drakov.lending.utils.validation.ArgsValidator;
import com.drakov.lending.dto.LendingResponse;
import com.drakov.lending.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonLendingController extends AbstractLendingController {

    private ModelService modelService;
    private LendingService lendingService;
    private LoanProperties props;

    @Autowired
    public CommonLendingController(ModelService modelService, LendingService lendingService, LoanProperties props) {
        this.modelService = modelService;
        this.lendingService = lendingService;
        this.props = props;
    }

    public ResponseEntity<LendingResponse> processLendingRequest(String... args) {
        return processCommonRequest(this::processInternal, args);
    }

    protected LendingResponse processInternal(String[] args) throws UserException {
        ArgsValidator.validateFileAndAmountArgs(props, args);

        String fileName = args[0];
        double loanAmount = Double.parseDouble(args[1]);

        modelService.uploadModelDataFile(fileName);

        return lendingService.calculateLoan(loanAmount);
    }


}
