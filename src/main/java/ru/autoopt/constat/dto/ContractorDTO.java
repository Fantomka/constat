package ru.autoopt.constat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContractorDTO {

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 10, message = "Длина ИНН Юридического лица должна быть ровно 10 цифр.")
    @ToString.Include
    private String INN;
    //private final String INN;

    // Балл - оценка.
    // Оценка выставляется в соответствии с ренджем балла:
    // 0 - 10, 11 - 20, 21 - 35, 36 - 50
    // Больше информации в таблице просчёта балла
    @ToString.Include
    private int rate;
    //private final int rate;

    // Имя организации, используется для вывода на фронте
    @ToString.Include
    private String orgName;

    // Ссылка на контрагента в контуре, используется на фронте
    private String focusHref;

    // Дата основания компании
    private Date foundingDate;

    // Имеет ли контрагент несколько ЮР адресов
    private boolean hasMassOrgAddress;

    // Имеет ли сайты
    private boolean hasSites;

    //Дата последней смены собственника
    private Date lastHeadChange;

    // Количество сотрудников
    // NOTE:API не всегда имеет это поле, может быть null
    private int countEmployees;

    // Выручка
    private long revenue;

    // Имеется ли лизинг
    private boolean hasLeasing;

    // Имеются ли госзакупки за последний год
    // Не всегда поле имеется, так что поле Nullable
    private boolean hasStateProcurements;

    // Предоставлял ли контрагент кому-то залоги
    private boolean hasPledges;

    // Имеет ли товарные знаки
    private boolean hasTradeMarks;

    // Сумма судов в качестве ответчика за последний год
    private long sumPetitionersOfArbitration;

    // Запасы торговые
    private long stockIndustry;

    // Дебиторская задолженность
    private long receivables;

    // Краткосрочные заемные средства
    private long ShortTermBorrowedFunds;

    // Кредиторская задолженность
    private long accountsPayable;

    // Валовая прибыль
    private long grossProfit;

    // Чистая прибыль
    private long netIncome;

    public void setINN(String INN) {
        if(this.INN == null) {
            this.INN = INN;
        }
    }

    public void setRate(int rate) {
        if(this.rate == 0) {
            this.rate = rate;
        }
    }
}
