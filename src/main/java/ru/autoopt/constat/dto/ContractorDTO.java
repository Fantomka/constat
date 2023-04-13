package ru.autoopt.constat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

//TODO Прописать здесь все поля, необходимые к задаче
public class ContractorDTO {

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 12, message = "Длина ИНН может быть либо 10 символов, либо 12")
    private String INN;

    private int rate;

}
