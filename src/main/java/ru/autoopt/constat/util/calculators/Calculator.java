package ru.autoopt.constat.util.calculators;

import ru.autoopt.constat.dto.ContractorDTO;

public interface Calculator {

    // TODO по некоторым полям калькуляция балла досрочно приостанавляется,
    //  пользователю нужно вывести сообщение в связи с чем.
    //  Продумать как интерфейс это будет выдавать
    //  Возможно стоит реализовать enum с кодами ошибок,
    //  по которому будет понятно что не так

    StatusCode calculate(ContractorDTO contractorDTO);
}
