package com.example.appproduitsinfo.services;

import com.example.appproduitsinfo.model.Facture;
import com.example.appproduitsinfo.model.Produit;
import com.example.appproduitsinfo.repository.FactureRepository;
import com.example.appproduitsinfo.repository.ProduitRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ProduitServices {
    public List<Produit> getProduitsLesPlusVendusGlobalement(FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAll();
        Map<Produit, Integer> ventesProduits = new HashMap<>();

        for (Facture facture : factures) {
            for (Produit produit : facture.getProduits()) {
                ventesProduits.put(produit, ventesProduits.getOrDefault(produit, 0) + 1);
            }
        }

        List<Produit> produitsLesPlusVendus = ventesProduits.entrySet().stream()
                .sorted(Map.Entry.<Produit, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return produitsLesPlusVendus;
    }

    public Map<Integer, List<Produit>> getProduitsLesPlusVendusParAnnee(FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAll();
        Map<Integer, Map<Produit, Integer>> ventesProduitsParAnnee = new HashMap<>();

        // Groupage des ventes par produit pour chaque année
        for (Facture facture : factures) {
            int annee = facture.getDate().getYear();

            Map<Produit, Integer> ventesProduits = ventesProduitsParAnnee.getOrDefault(annee, new HashMap<>());
            for (Produit produit : facture.getProduits()) {
                ventesProduits.put(produit, ventesProduits.getOrDefault(produit, 0) + 1);
            }
            ventesProduitsParAnnee.put(annee, ventesProduits);
        }

        // Obtention des produits les plus vendus par année
        Map<Integer, List<Produit>> produitsLesPlusVendusParAnnee = new HashMap<>();
        for (Map.Entry<Integer, Map<Produit, Integer>> entry : ventesProduitsParAnnee.entrySet()) {
            int annee = entry.getKey();
            Map<Produit, Integer> ventesProduits = entry.getValue();

            List<Produit> produitsLesPlusVendus = ventesProduits.entrySet().stream()
                    .sorted(Map.Entry.<Produit, Integer>comparingByValue().reversed())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            produitsLesPlusVendusParAnnee.put(annee, produitsLesPlusVendus);
        }

        return produitsLesPlusVendusParAnnee;
    }



}
