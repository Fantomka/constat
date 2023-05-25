package ru.autoopt.constat.services.models;

import java.util.ArrayList;
import java.util.List;

public class StatementForm {

    private String resultString;

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

    public StatementForm() {
        this.additionalWarnings = new ArrayList<>();
        this.additionalInfo = new ArrayList<>();
    }
}
