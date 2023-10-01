package ru.autoopt.constat.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.services.models.ContractorService;
import ru.autoopt.constat.util.validators.ContractorAlreadyExistsValidator;
import ru.autoopt.constat.util.validators.ContractorNotExistsValidator;

@Controller
@RequestMapping("/contractors")
@AllArgsConstructor
public class ContractorController {

    private final ContractorService contractorService;
    private final ContractorAlreadyExistsValidator contractorAlreadyExistsValidator;
    private final ContractorNotExistsValidator contractorNotExistsValidator;

    @GetMapping()
    public String index(@ModelAttribute("contractor") ContractorDTO contractor) {
        return "contractors_v1/menu";
    }

    @GetMapping("/new")
    public String newContractor(@ModelAttribute("contractor") ContractorDTO contractorDTO) {
        return "contractors_v1/new";
    }

    @PostMapping("/new")
    public String newContractor(
            Model model,
            @ModelAttribute("contractor") @Valid ContractorDTO contractor,
            BindingResult bindingResult
    ) {
        contractorAlreadyExistsValidator.validate(contractor, bindingResult);
        if (bindingResult.hasErrors())
            return "contractors_v1/new";

        contractor.setRate(0);
        model.addAttribute("result", contractorService.counterpartyVerification(contractor));

        return "contractors_v1/new";
    }

    @GetMapping("/recalculate")
    public String recalculateContractor() { return "contractors_v1/recalculate"; }

    @PostMapping("/recalculate")
    public String recalculateContractor(
            Model model,
            @RequestParam(value = "queryString") String queryString
//            BindingResult bindingResult
    ) {
//        contractorNotExistsValidator.validate(contractor, bindingResult);
//        if (bindingResult.hasErrors())
//            return "contractors_v1/recalculate";

        model.addAttribute("result", contractorService.recalculate(queryString));
        return "contractors_v1/recalculate";
    }

    @GetMapping("/check")
    public String checkContractors(Model model) {

        model.addAttribute("contractors", contractorService.indexContractorsInDangerZone());

        return "contractors_v1/check";
    }

}
