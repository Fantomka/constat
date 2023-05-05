package ru.autoopt.constat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "contractor_rating")
@NoArgsConstructor
@Data
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 10, message = "Длина ИНН Юридического лица должна быть ровно 10 цифр.")
    @Column(name = "INN")
    private String INN;

    @Column(name = "rate")
    private int rate;

    @OneToMany(mappedBy = "contractor")
    private List<ContractRecord> contracts;

    public Contractor(String INN, int rate) {
        this.INN = INN;
        this.rate = rate;
    }
}
