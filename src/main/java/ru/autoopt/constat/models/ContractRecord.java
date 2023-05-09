package ru.autoopt.constat.models;

import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="contracts")
public class ContractRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "given_at")
    @Temporal(TemporalType.DATE)
    private Date givenAt;

    @Column(name = "expires_after")
    private int expiresAfter;

    @Column(name = "days_overdue")
    private int daysOverdue;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "contractor_id", referencedColumnName = "id")
    private Contractor contractor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGivenAt() {
        return givenAt;
    }

    public void setGivenAt(Date givenAt) {
        this.givenAt = givenAt;
    }

    public int getExpiresAfter() {
        return expiresAfter;
    }

    public void setExpiresAfter(int expiresAfter) {
        this.expiresAfter = expiresAfter;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(int daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public ContractRecord() {
    }

    public ContractRecord(Date givenAt, int expiresAfter, int daysOverdue) {
        this.givenAt = givenAt;
        this.expiresAfter = expiresAfter;
        this.daysOverdue = daysOverdue;
    }

    public ContractRecord(String givenAt, int expiresAfter, int daysOverdue, Contractor contractor) {
        this(givenAt, expiresAfter, daysOverdue);
        this.contractor = contractor;
    }

    public ContractRecord(String givenAt, int expiresAfter, int daysOverdue) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.givenAt = formatter.parse(givenAt);
        } catch (ParseException ignored) {}
        this.expiresAfter = expiresAfter;
        this.daysOverdue = daysOverdue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractRecord that = (ContractRecord) o;
        return expiresAfter == that.expiresAfter &&
                Objects.equals(id, that.id) &&
                Objects.equals(givenAt, that.givenAt) &&
                Objects.equals(daysOverdue, that.daysOverdue) &&
                Objects.equals(contractor, that.contractor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, givenAt, expiresAfter, daysOverdue, contractor);
    }
}
