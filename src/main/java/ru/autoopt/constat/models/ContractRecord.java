package ru.autoopt.constat.models;

import jakarta.persistence.*;

import java.util.Date;

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

    @Column(name = "expires_at")
    @Temporal(TemporalType.DATE)
    private Date expiresAt;

    @Column(name = "paid_at")
    @Temporal(TemporalType.DATE)
    private Date PaidAt;

    @Column(name = "rate")
    private int rate; // Балл скоринга, который был выдан при оформлении кредита


    @ManyToOne
    @JoinColumn(name = "contractor_id", referencedColumnName = "id")
    private Contractor contractor;

}
