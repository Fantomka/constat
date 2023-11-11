package ru.autoopt.constat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.autoopt.constat.models.Contractor;

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
    private Integer rate;

    // Имя организации, используется для вывода на фронте
    @ToString.Include
    private String orgName;

    // Ссылка на контрагента в контуре, используется на фронте
    private String focusHref;

    //************** Поля с проверками параметром (задаются в enrichers) **************

    // Дата основания компании до 01.07.2022
    private Boolean foundingDateOk;

    private Boolean isStatusOk;

    // Имеет ли контрагент несколько ЮР адресов
    private Boolean hasMassOrgAddress;

    // Имеет ли сайты
    private String site;

    private Boolean hasSites;

    // Дата изменения собственника до 01.07.2022
    private Boolean lastHeadChangeDateOk;

    // Количество сотрудников
    // NOTE:API не всегда имеет это поле, может быть null
    private Boolean countEmployeesEnough;

    // Выручка проходит условие
    private Boolean isRevenueOk;

    // Выручка
    private long revenue;

    // Имеется ли лизинг
    private Boolean isLeasingOk;

    // Имеются ли госзакупки за последний год
    // Не всегда поле имеется, так что поле Nullable
    private Boolean hasStateProcurements;

    // Предоставлял ли контрагент кому-то залоги
    private Boolean isPledgesOk;

    // Имеет ли товарные знаки
    private Boolean hasTradeMarks;

    // Сумма судов в качестве ответчика за последний год
    private Boolean isSumPetitionersOfArbitrationOk;

    // Основные средства
    private Boolean isMainFoundOk;

    // Запасы торговые
    private Boolean isStockIndustryOk;

    // Дебиторская задолженность
    private Boolean isReceivablesOk;

    // Кредиторская задолженность
    private Boolean isAccountsPayableOk;

    // Валовая прибыль
    private Boolean isGrossProfitOk;

    // Чистая прибыль
    private Boolean isNetIncomeOk;

    public Contractor toEntity() {
        return new Contractor(this.INN, this.orgName, this.rate);
    }

    public void setINN(String INN) {
        if(this.INN == null) {
            this.INN = INN;
        }
    }
}
