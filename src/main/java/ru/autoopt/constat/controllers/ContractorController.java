package ru.autoopt.constat.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.services.models.ContractorService;
import ru.autoopt.constat.util.validators.ContractorValidator;

@Controller
@RequestMapping("/contractors")
@AllArgsConstructor
public class ContractorController {

    private final ContractorService contractorService;
    private final ContractorValidator contractorValidator;

    @GetMapping()
    public String index(@ModelAttribute("contractor") ContractorDTO contractor) {
        System.out.println();
        return "contractors_v1/index";
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
        System.out.println("new contractor");

        contractorValidator.validate(contractor, bindingResult);
        if (bindingResult.hasErrors())
            return "contractors_v1/new";

        contractor.setRate(0);
        model.addAttribute("text", contractorService.counterpartyVerification(contractor));

        //contractorService.save(contractor);
        return "contractors_v1/new";
    }
}
