package com.example.appproduitsinfo.controller;

import com.example.appproduitsinfo.model.Categorie;
import com.example.appproduitsinfo.model.Produit;
import com.example.appproduitsinfo.repository.CategorieRepository;
import com.example.appproduitsinfo.repository.FactureRepository;
import com.example.appproduitsinfo.repository.ProduitRepository;
import com.example.appproduitsinfo.services.ProduitServices;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/produits")
public class ProduitController {
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private CategorieRepository categorieRepository;

    @RequestMapping(value = "/")
    public String index(Model model) {
        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("produits", produits);

        return "produit/list";
    }

    @RequestMapping(value = "/en_repture_stock")
    public String getListeProduitsEnReptureStock(Model model) {
        List<Produit> produits = produitRepository.findAll();
        List<Produit> produitsEnRepture = new ArrayList<>();

        for (Produit produit : produits){
            if (produit.getStockDisponible() == 0){
                produitsEnRepture.add(produit);
            }
        }

        model.addAttribute("produits", produitsEnRepture);
        return "produit/repture_stock";
    }

    @RequestMapping(value = "/plus_vendus")
    public String getListeProduitsPlusVendus(Model model) {
        ProduitServices produitServices = new ProduitServices();
        List<Produit> produitsLesPlusVendusGlobalement = produitServices.getProduitsLesPlusVendusGlobalement(factureRepository);
        Map<Integer, List<Produit>> produitsLesPlusVendusParAnnee = produitServices.getProduitsLesPlusVendusParAnnee(factureRepository);
        model.addAttribute("produitsLesPlusVendusGlobalement", produitsLesPlusVendusGlobalement);
        model.addAttribute("produitsLesPlusVendusParAnnee", produitsLesPlusVendusParAnnee);

        return "produit/list_plus_vendus";
    }


    @RequestMapping(value = "/new")
    public String newProduit(Model model) {

        List<Categorie> categorie = categorieRepository.findAll();
        model.addAttribute("categories", categorie);
        return "produit/new";
    }

    @PostMapping("/save")
    public String saveProduit(HttpServletRequest request) {
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");
        double prix = Double.parseDouble(request.getParameter("prix"));
        int stockDisponible = Integer.parseInt(request.getParameter("stockDisponible"));
        long categorie_id = Long.parseLong(request.getParameter("categorie"));

        Categorie categorie = categorieRepository.findById(categorie_id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie non trouvé avec l'ID : " + categorie_id));

        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setDescription(description);
        produit.setPrix(prix);
        produit.setStockDisponible(stockDisponible);
        produit.setCategorie(categorie);
        produitRepository.save(produit);
        return "redirect:/produits/";
    }

    // Endpoint pour afficher le formulaire de mise à jour du produit
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID : " + id));

        model.addAttribute("produit", produit);
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("categories", categories);
        return "produit/update";
    }

    // Endpoint pour traiter la mise à jour du produit
    @PostMapping("/update/{id}")
    public String updateProduit(@PathVariable("id") Long id, HttpServletRequest request) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé avec l'ID : " + id));

        String nom = request.getParameter("nom");
        String description = request.getParameter("description");
        double prix = Double.parseDouble(request.getParameter("prix"));
        int stockDisponible = Integer.parseInt(request.getParameter("stockDisponible"));
        long categorie_id = Long.parseLong(request.getParameter("categorie"));
        Categorie categorie = categorieRepository.findById(categorie_id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie non trouvé avec l'ID : " + categorie_id));

        produit.setNom(nom);
        produit.setDescription(description);
        produit.setPrix(prix);
        produit.setStockDisponible(stockDisponible);
        produit.setCategorie(categorie);

        produitRepository.save(produit);
        return "redirect:/produits/";
    }

    // Supprimer un produit
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        produitRepository.deleteById(id);
        return "redirect:/produits/";
    }

}
