package com.example.appproduitsinfo.repository;

import com.example.appproduitsinfo.model.Client;
import com.example.appproduitsinfo.model.Facture;
import com.example.appproduitsinfo.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findAllByClient(Client client);


    List<LocalDate> findDistinctDateByClient(Client client);

    List<Facture> findByClient(Client client);

    long countByClient(Client client);

}
