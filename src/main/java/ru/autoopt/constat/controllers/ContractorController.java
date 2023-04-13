package ru.autoopt.constat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.autoopt.constat.services.ContractorService;

@Controller
@RequestMapping("/contractor")
public class ContractorController {

    private final ContractorService contractorService;

    @Autowired
    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("contractors", contractorService.index());

        return "contractors/index";
    }
}
