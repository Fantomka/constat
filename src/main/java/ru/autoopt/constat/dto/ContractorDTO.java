package ru.autoopt.constat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//TODO Прописать здесь все поля, необходимые к задаче
public class ContractorDTO {

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 12, message = "Длина ИНН может быть либо 10 символов, либо 12")
    private String INN;
    private int rate;
    private String orgName;
    private String ogrn;
    private String focusHref;
    private String address;
    private String foundingDate;

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getFocusHref() {
        return focusHref;
    }

    public void setFocusHref(String focusHref) {
        this.focusHref = focusHref;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFoundingDate() {
        return foundingDate;
    }

    public void setFoundingDate(String foundingDate) {
        this.foundingDate = foundingDate;
    }

    public ContractorDTO(String INN, int rate) {
        this.INN = INN;
        this.rate = rate;
    }

    public ContractorDTO() {
    }
}
