package ru.autoopt.constat.services.models;

import ru.autoopt.constat.dto.ContractorDTO;

import java.util.ArrayList;
import java.util.List;

public class StatementForm {

    private String resultString;

    private ContractorDTO contractorDTO;

    private List<String> additionalInfo;

    private List<String> additionalWarnings;

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public List<String> getAdditionalWarnings() {
        return additionalWarnings;
    }

    public void setAdditionalWarnings(List<String> additionalWarnings) {
        this.additionalWarnings = additionalWarnings;
    }

    public List<String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public ContractorDTO getContractorDTO() {
        return contractorDTO;
    }

    public void setContractorDTO(ContractorDTO contractorDTO) {
        this.contractorDTO = contractorDTO;
    }

    public StatementForm() {
        this.additionalWarnings = new ArrayList<>();
        this.additionalInfo = new ArrayList<>();
    }
}
