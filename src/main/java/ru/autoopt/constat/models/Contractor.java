package ru.autoopt.constat.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "contractor_rating")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 10, message = "Длина ИНН Юридического лица должна быть ровно 10 цифр.")
    @Column(name = "INN")
    private String INN;

    @NotEmpty(message = "Компания не может не иметь названия")
    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private int rate;

    @OneToMany(mappedBy = "contractor")
    private List<ContractRecord> contracts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContractRecord> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractRecord> contracts) {
        this.contracts = contracts;
    }

    public Contractor() {
    }

    public Contractor(String INN, String name, int rate) {
        this.INN = INN;
        this.name = name;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contractor that = (Contractor) o;
        return rate == that.rate && id.equals(that.id) && INN.equals(that.INN) && Objects.equals(contracts, that.contracts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, INN, rate, contracts);
    }
}
