package ru.autoopt.constat.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.Contractor;
import ru.autoopt.constat.services.models.ContractorService;
import ru.autoopt.constat.util.validators.ContractorAlreadyExistsValidator;
import ru.autoopt.constat.util.validators.ContractorNotExistsValidator;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/contractors")
@AllArgsConstructor
public class ContractorController {

    private final ContractorService contractorService;
    private final ContractorAlreadyExistsValidator contractorAlreadyExistsValidator;
    private final ContractorNotExistsValidator contractorNotExistsValidator;

    @GetMapping()
    public String index() {
        return "contractors_v1/menu";
    }

    @GetMapping("/list")
    public String contractorsIndex(
            Model model,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "contractors_per_page", required = false)
            @Max(value = 20, message = "Количество контрагентов не должно превышать 20")
            Integer contractorsPerPage
    ) {
        List<Contractor> contractors = contractorService.index(page, contractorsPerPage);
        model.addAttribute("contractors", contractors);
        model.addAttribute("savedPage", Objects.requireNonNullElse(page, 1));
        model.addAttribute("savedContractorsPerPage", Objects.requireNonNullElse(contractorsPerPage, contractors.size()));

        return "contractors_v1/index";
    }

    @GetMapping("/check/new")
    public String checkNewContractor(@ModelAttribute("contractor") ContractorDTO contractorDTO) {
        return "contractors_v1/check/new";
    }

    @PostMapping("/check/new")
    public String checkNewContractor(
            Model model,
            @ModelAttribute("contractor") @Valid ContractorDTO contractor,
            BindingResult bindingResult
    ) {
        contractorAlreadyExistsValidator.validate(contractor, bindingResult);
        if (bindingResult.hasErrors())
            return "contractors_v1/check/new";

        contractor.setRate(0);
        model.addAttribute("result", contractorService.counterpartyVerification(contractor));

        return "contractors_v1/check/new";
    }

    @PostMapping("/add/new/{inn}")
    public String create(
        @PathVariable("inn") String inn,
        @RequestParam(value = "rate") int rate,
        @RequestParam(value = "orgName") String orgName
    ) {
        ContractorDTO contractorDTO = new ContractorDTO();
        contractorDTO.setINN(inn);
        contractorDTO.setOrgName(orgName);
        contractorDTO.setRate(rate);
        System.out.println(contractorDTO);
        contractorService.save(contractorDTO);
        return "redirect:/contractors/list";
    }

    @GetMapping("/check/existing")
    public String recalculateContractor(@ModelAttribute("contractor") ContractorDTO contractorDTO) {
        return "contractors_v1/check/existing";
    }

    @PostMapping("/check/existing")
    public String recalculateContractor(
            Model model,
            @ModelAttribute("contractor") @Valid ContractorDTO contractor,
            BindingResult bindingResult
    ) {
        contractorNotExistsValidator.validate(contractor, bindingResult);
        if (bindingResult.hasErrors())
            return "contractors_v1/check/existing";

        contractor.setRate(0);
        model.addAttribute("result", contractorService.recalculate(contractor));
        return "contractors_v1/check/existing";
    }

    @GetMapping("/check/danger-zone")
    public String checkContractors(Model model) {

        model.addAttribute("contractors", contractorService.indexContractorsInDangerZone());

        return "contractors_v1/check/danger_zone";
    }

}
