package ru.autoopt.constat.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "contractor_rating")
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "ИНН - обязательное поле")
    @Size(min = 10, max = 12, message = "Длина ИНН может быть либо 10 символов, либо 12")
    @Column(name = "INN")
    private String INN;

    @Column(name = "rate")
    private int rate;

    public Contractor(int id, String INN, int rate) {
        this.id = id;
        this.INN = INN;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contractor that = (Contractor) o;
        return id == that.id && rate == that.rate && INN.equals(that.INN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, INN, rate);
    }

    @Override
    public String toString() {
        return "Contractor{" +
                "id=" + id +
                ", INN='" + INN + '\'' +
                ", rate=" + rate +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Contractor() {
    }
}
