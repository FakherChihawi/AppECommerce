package com.example.appproduitsinfo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reglement {

    public enum Status {
        ENATTENTE("En Attente"),
        ENCOURS("En Cours"),
        TERMINE("Terminé");

        private final String label; // Ajout d'un champ pour stocker les valeurs

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "devise_id")
    private Devise devise; // Référence vers la devise utilisée pour le règlement

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture; // Référence vers la facture associée à ce règlement

    public Reglement(Devise devise, Facture facture) {
        this.date = LocalDate.now();
        this.status = Status.ENATTENTE;
        this.devise = devise;
        this.facture = facture;
    }

    public Reglement(LocalDate date, Status status) {
        this.date = date;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
}
