package com.example.appproduitsinfo.repository;

import com.example.appproduitsinfo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClientRepository extends JpaRepository<Client, Long> {}