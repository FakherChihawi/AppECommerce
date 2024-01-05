package com.example.appproduitsinfo.repository;

import com.example.appproduitsinfo.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findAllByStockDisponible(int i);
}