package com.example.appproduitsinfo.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reglement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double montant;

    @ManyToOne
    @JoinColumn(name = "devise_id")
    private Devise devise; // Référence vers la devise utilisée pour le règlement


    public Reglement(LocalDate date, double montant) {
        this.date = date;
        this.montant = montant;
    }

    public Reglement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }
}
