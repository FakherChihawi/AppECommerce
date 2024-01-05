package com.example.appproduitsinfo.repository;

import com.example.appproduitsinfo.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategorieRepository extends JpaRepository<Categorie, Long> {}