package ru.autoopt.constat.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.autoopt.constat.dto.ContractorDTO;
import ru.autoopt.constat.models.ContractRecord;
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
        contractorService.save(createContractorDTO(inn, orgName, rate));
        return "redirect:/contractors/list";
    }

    @GetMapping("/check/existing")
    public String recalculateContractor(@ModelAttribute("contractor") ContractorDTO contractorDTO) {
        return "contractors_v1/check/existing";
    }

    @GetMapping("/check/existing/{inn}")
    public String existingContractorFromExternal(
        Model model,
        @PathVariable("inn") String inn,
        @RequestParam(value = "orgName") String orgName,
        @RequestParam(value = "addContract", required = false) Boolean addContract,
        @RequestParam(value = "contractToEdit", required = false) Long contractToEdit,
        @ModelAttribute("updatedContract") ContractRecord contractRecord,
        @ModelAttribute("contract") @Valid ContractRecord contract
    ) {
        ContractorDTO contractorDTO = createContractorDTO(inn, orgName, 0);
        model.addAttribute("result", contractorService.recalculate(contractorDTO));
        model.addAttribute("contractor", contractorDTO);
        model.addAttribute("contracts", contractorService.getContractsByINN(contractorDTO.getINN()));
        model.addAttribute("addContract", Objects.requireNonNullElse(addContract, false));
        model.addAttribute("contractToEdit", contractToEdit);
        return "contractors_v1/check/existing";
    }


    @PostMapping("/check/existing")
    public String recalculateContractor(
        Model model,
        @ModelAttribute("contractor") @Valid ContractorDTO contractorDTO,
        BindingResult bindingResult
    ) {
        contractorNotExistsValidator.validate(contractorDTO, bindingResult);
        if (bindingResult.hasErrors())
            return "contractors_v1/check/existing";

        contractorDTO.setRate(0);
        model.addAttribute("result", contractorService.recalculate(contractorDTO));
        model.addAttribute("contracts", contractorService.getContractsByINN(contractorDTO.getINN()));
        return "contractors_v1/check/existing";
    }


    @PatchMapping("/update/contract/{inn}")
    public String updateContract(
            @PathVariable("inn") String inn,
            @RequestParam(value = "id") Long id,
            @ModelAttribute("updatedContract") ContractRecord updatedContract,
            @RequestParam(value = "orgName") String orgName,
            RedirectAttributes redirectAttributes
    ) {
        contractorService.updateContract(id, updatedContract);
        redirectAttributes.addAttribute("orgName", orgName);
        return "redirect:/contractors/check/existing/" + inn;
    }

    @DeleteMapping("/delete/contract/{inn}")
    public String deleteContract(
        @PathVariable("inn") String inn,
        @RequestParam(value = "id") Long id,
        @RequestParam(value = "orgName") String orgName,
        RedirectAttributes redirectAttributes
    ) {
        contractorService.deleteContractById(id);
        redirectAttributes.addAttribute("orgName", orgName);
        return "redirect:/contractors/check/existing/" + inn;
    }

    @PostMapping("/add/contract/{inn}")
    public String recalculateContractor(
        @PathVariable("inn") String inn,
        @RequestParam(value = "orgName") String orgName,
        @RequestParam(value = "givenAt") String givenAt,
        @RequestParam(value = "expiresAfter") Integer expiresAfter,
        @RequestParam(value = "daysOverdue") Integer daysOverdue,
        RedirectAttributes redirectAttributes
    ) {
        ContractorDTO contractorDTO = createContractorDTO(inn, orgName, 0);
        ContractRecord contract = new ContractRecord(givenAt, expiresAfter, daysOverdue);
        contractorService.addContract(contractorDTO, contract);
        redirectAttributes.addAttribute("orgName", orgName);
        return "redirect:/contractors/check/existing/" + inn;
    }

    @DeleteMapping("/delete/existing/{inn}")
    public String deleteExistingContractor(@PathVariable("inn") String inn) {
        contractorService.deleteContractorByInn(inn);
        return "redirect:/contractors/list";
    }
    @GetMapping("/check/danger-zone")
    public String checkContractors(Model model) {
        model.addAttribute("contractors", contractorService.indexContractorsInDangerZone());
        return "contractors_v1/check/danger_zone";
    }

    private ContractorDTO createContractorDTO(String inn, String orgName, int rate) {
        ContractorDTO contractorDTO = new ContractorDTO();
        contractorDTO.setINN(inn);
        contractorDTO.setOrgName(orgName);
        contractorDTO.setRate(rate);
        return contractorDTO;
    }

}
