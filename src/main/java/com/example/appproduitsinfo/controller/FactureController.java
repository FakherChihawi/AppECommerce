package com.example.appproduitsinfo.controller;
import com.example.appproduitsinfo.model.Client;
import com.example.appproduitsinfo.model.Facture;
import com.example.appproduitsinfo.model.Produit;
import com.example.appproduitsinfo.repository.ClientRepository;
import com.example.appproduitsinfo.repository.FactureRepository;
import com.example.appproduitsinfo.repository.ProduitRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping (value = "/factures")
public class FactureController {
    @Autowired
    private FactureRepository factureRepository;


    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping (value = "/")
    public String index(Model model) {
        List<Facture> factures = factureRepository.findAll();
        model.addAttribute("factures", factures);
        return "facture/list";
    }

    @RequestMapping (value = "/filter_reglee")
    public String filterReglee(Model model) {
        List<Facture> factures = factureRepository.findAll();
        List<Facture> facturesReglees = new ArrayList<>();
        List<Facture> facturesNonReglees = new ArrayList<>();

        for (Facture facture : factures){
            if (facture.getReglements().isEmpty()){
                facturesNonReglees.add(facture);
            }else{
                facturesReglees.add(facture);
            }
        }

        model.addAttribute("facturesReglees", facturesReglees);
        model.addAttribute("facturesNonReglees", facturesNonReglees);
        return "facture/filter_reglee";
    }

    @RequestMapping (value = "/new")
    public String newFacture (Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);

        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("produits", produits);

        return "facture/new";
    }

    @PostMapping("/save")
    public String saveFacture(HttpServletRequest request) {
        LocalDate date = LocalDate.now();
        long client_id = Long.parseLong(request.getParameter("clients"));
        String etat = request.getParameter("etat");
        Client client = clientRepository.findById(client_id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + client_id));
        double montantTotal = 0;
        Facture facture = new Facture(date,client);

        String[] produitsIds = request.getParameterValues("produits");
        List<Produit> produitsList = new ArrayList<>();


        if (produitsIds != null) {
            for (String produitId : produitsIds) {
                long produitIdLong = Long.parseLong(produitId);
                Produit produit = produitRepository.findById(produitIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID : " + produitIdLong));
                montantTotal += produit.getPrix();
                produitsList.add(produit);
            }
        }

        facture.setEstPayee(Objects.equals(etat, "PAYEE"));
        facture.setProduits(produitsList);
        facture.setMontantTotal(montantTotal);

        factureRepository.save(facture);
        return "redirect:/factures/";
    }

    // Endpoint pour afficher le formulaire de mise à jour du produit
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvé avec l'ID : " + id));
        model.addAttribute("facture", facture);

        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);

        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("produits", produits);

        return "facture/update";
    }

    // Endpoint pour traiter la mise à jour du produit
    @PostMapping("/update/{id}")
    public String updateFacture(@PathVariable("id") Long id, HttpServletRequest request) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("facture non trouvé avec l'ID : " + id));

        facture.getProduits().clear();
        long client_id = Long.parseLong(request.getParameter("clients"));
        Client client = clientRepository.findById(client_id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + client_id));

        String etat = request.getParameter("etat");

        double montantTotal = 0;

        String[] produitsIds = request.getParameterValues("produits");
        List<Produit> produitsList = new ArrayList<>();

        if (produitsIds != null) {
            for (String produitId : produitsIds) {
                long produitIdLong = Long.parseLong(produitId);
                Produit produit = produitRepository.findById(produitIdLong)
                        .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID : " + produitIdLong));
                montantTotal += produit.getPrix();
                produitsList.add(produit);
            }
        }

        facture.setEstPayee(Objects.equals(etat, "PAYEE"));
        facture.setProduits(produitsList);
        facture.setMontantTotal(montantTotal);
        facture.setClient(client);

        factureRepository.save(facture);
        return "redirect:/factures/";
    }

    // Supprimer un facture
    @GetMapping("/delete/{id}")
    public String deleteFacture(@PathVariable Long id) {
        factureRepository.deleteById(id);
        return "redirect:/factures/";
    }
}
