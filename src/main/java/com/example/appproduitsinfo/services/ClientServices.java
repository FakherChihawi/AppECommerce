package com.example.appproduitsinfo.services;

import com.example.appproduitsinfo.model.Client;
import com.example.appproduitsinfo.model.Facture;
import com.example.appproduitsinfo.model.Produit;
import com.example.appproduitsinfo.repository.ClientRepository;
import com.example.appproduitsinfo.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ClientServices {

    public Double getChiffreAffaireGlobal(Client client, FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAllByClient(client);
        return factures.stream()
                .mapToDouble(Facture::getMontantTotal)
                .sum();
    }

    public Double getResteGlobaleNonPayee(Client client, FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAllByClient(client);
        double reste = 0.0;
        for (Facture facture : factures) {
            if (!facture.isEstPayee()) {
                reste += facture.getMontantTotal();
            }
        }
        return reste;
    }


    public Map<Integer, Double> getAffairesParClientEtAnnee(Client client, FactureRepository factureRepository) {

        List<Facture> factures = factureRepository.findAllByClient(client);


        ArrayList<Integer> annees = new ArrayList<>();
        for (Facture facture : factures) {
            int annee = facture.getDate().getYear();
            if (!annees.contains(annee)) {
                annees.add(annee);
            }
        }

        Map<Integer, Double> chiffreAffairesParAnnee = new HashMap<>();
        for (Integer annee : annees) {
            double chiffre = 0.0;

            for (Facture facture : factures) {
                if (facture.getDate().getYear() == annee) {
                    chiffre += facture.getMontantTotal();
                }
            }

            chiffreAffairesParAnnee.put(annee, chiffre);
        }


        return chiffreAffairesParAnnee;
    }

    public List<Facture> getFactureRegleee(Client client, FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAllByClient(client);
        List<Facture> facturesReglee = new ArrayList<>();
        for (Facture facture : factures) {
            if (!facture.getReglements().isEmpty()) {
                facturesReglee.add(facture);
            }
        }
        return facturesReglee;
    }

    public List<Facture> getFactureNonRegleee(Client client, FactureRepository factureRepository) {
        List<Facture> factures = factureRepository.findAllByClient(client);
        List<Facture> facturesReglee = new ArrayList<>();
        for (Facture facture : factures) {
            if (facture.getReglements().isEmpty()) {
                facturesReglee.add(facture);
            }
        }
        return facturesReglee;
    }

    public List<Produit> getProduitsLesPlusSollicites(Client client, FactureRepository factureRepository) {
        List<Facture> facturesClient = factureRepository.findByClient(client);
        List<Produit> produitsClient = new ArrayList<>();
        for (Facture facture : facturesClient) {
            produitsClient.addAll(facture.getProduits());
        }

        // Utilisation d'un Map pour stocker les produits et leur comptage
        Map<Produit, Integer> produitsSollicites = new HashMap<>();
        for (Produit produit : produitsClient) {
            produitsSollicites.put(produit, produitsSollicites.getOrDefault(produit, 0) + 1);
        }

        // Trier les produits par le nombre d'occurrences
        List<Map.Entry<Produit, Integer>> produitsTries = new ArrayList<>(produitsSollicites.entrySet());
        produitsTries.sort(Map.Entry.<Produit, Integer>comparingByValue().reversed());

        // Prendre les 5 produits les plus sollicités, ajustez la limite si nécessaire
        List<Produit> produitsLesPlusSollicites = new ArrayList<>();
        int limit = Math.min(5, produitsTries.size());
        for (int i = 0; i < limit; i++) {
            produitsLesPlusSollicites.add(produitsTries.get(i).getKey());
        }

        return produitsLesPlusSollicites;
    }

    public List<Client> getClientsLesPlusFideles(ClientRepository clientRepository, FactureRepository factureRepository) {
        List<Client> clients = clientRepository.findAll();
        Map<Client, Long> nombreTransactionsParClient = new HashMap<>();

        // Calcul du nombre de transactions par client
        for (Client client : clients) {
            long nombreTransactions = factureRepository.countByClient(client);
            nombreTransactionsParClient.put(client, nombreTransactions);
        }

        // Trie les clients par nombre de transactions
        List<Client> clientsFideles = nombreTransactionsParClient.entrySet().stream()
                .sorted(Map.Entry.<Client, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return clientsFideles;
    }

}
