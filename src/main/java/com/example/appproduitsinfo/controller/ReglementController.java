package com.example.appproduitsinfo.controller;

import com.example.appproduitsinfo.model.*;
import com.example.appproduitsinfo.repository.CategorieRepository;
import com.example.appproduitsinfo.repository.DeviseRepository;
import com.example.appproduitsinfo.repository.FactureRepository;
import com.example.appproduitsinfo.repository.ReglementRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/reglements")
public class ReglementController {
    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    DeviseRepository deviseRepository;

    @RequestMapping(value = "/")
    public String index(Model model) {
        List<Reglement> reglements = reglementRepository.findAll();
        model.addAttribute("reglements", reglements);
        return "reglement/list";
    }

    @RequestMapping(value = "/new")
    public String newReglement(Model model) {
        List<Devise> devises = deviseRepository.findAll();
        List<Facture> factures = factureRepository.findAll();

        model.addAttribute("devises", devises);
        model.addAttribute("factures", factures);
        return "reglement/new";
    }

    @PostMapping("/save")
    public String saveReglement(HttpServletRequest request) {
        Long idDevise = Long.valueOf(request.getParameter("factures"));
        Long idFacture = Long.valueOf(request.getParameter("devises"));

        Devise devise = deviseRepository.findById(idDevise)
                .orElseThrow(() -> new IllegalArgumentException("Devise non trouvé avec l'ID : " + idDevise));

        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvé avec l'ID : " + idFacture));


        Reglement reglement = new Reglement(devise, facture);

        reglementRepository.save(reglement);
        return "redirect:/reglements/";
    }


    // Endpoint pour afficher le formulaire de mise à jour du reglement
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Reglement reglement = reglementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Regelement non trouvé avec l'ID : " + id));

        List<Devise> devises = deviseRepository.findAll();
        List<Facture> factures = factureRepository.findAll();

        model.addAttribute("reglement", reglement);
        model.addAttribute("devises", devises);
        model.addAttribute("factures", factures);
        return "produit/update";
    }

    // Endpoint pour traiter la mise à jour du reglement
    @PostMapping("/update/{id}")
    public String updateReglement(@PathVariable("id") Long id, HttpServletRequest request) {
        Reglement reglement = reglementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Regelement non trouvé avec l'ID : " + id));

        Long idDevise = Long.valueOf(request.getParameter("factures"));
        Long idFacture = Long.valueOf(request.getParameter("devises"));

        Devise devise = deviseRepository.findById(idDevise)
                .orElseThrow(() -> new IllegalArgumentException("Devise non trouvé avec l'ID : " + idDevise));

        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvé avec l'ID : " + idFacture));


        reglement.setDevise(devise);
        reglement.setFacture(facture);

        reglementRepository.save(reglement);
        return "redirect:/produits/";
    }

}
