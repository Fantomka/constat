package ru.autoopt.constat.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.services.ContractorService;
import ru.autoopt.constat.util.validators.ContractorValidator;

@Controller
@RequestMapping("/contractors")
@AllArgsConstructor
public class ContractorController {

    private final ContractorService contractorService;
    private final ContractorValidator contractorValidator;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("contractors", contractorService.index());

        return "contractors/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("contractor") ContractorDTO contractor) {
        return "contractors/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("contractor") @Valid ContractorDTO contractor, BindingResult bindingResult) {
        contractorValidator.validate(contractor, bindingResult);

        if (bindingResult.hasErrors())
            return "contractors/new";

        contractorService.counterpartyVerification(contractor);
        //contractorService.save(contractor);
        return "redirect:contractors";
    }
}
