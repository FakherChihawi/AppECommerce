package com.example.appproduitsinfo.controller;
import com.example.appproduitsinfo.model.Categorie;
import com.example.appproduitsinfo.repository.CategorieRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping (value = "/categories")
public class CategorieControlleur {
    @Autowired
    private CategorieRepository categorieRepository;

    @RequestMapping (value = "/")
    public String index(Model model) {
        List<Categorie> categories = categorieRepository.findAll();
        model.addAttribute("categories", categories);
        return "categorie/list";
    }

    @RequestMapping (value = "/new")
    public String newCategorie ()
    {
        return "categorie/new";
    }

    @PostMapping("/save")
    public String saveCategorie(HttpServletRequest request) {
        String nom = request.getParameter("nom");

        Categorie categorie = new Categorie();
        categorie.setNom(nom);
        categorieRepository.save(categorie);
        return "redirect:/categories/";
    }

    // Endpoint pour afficher le formulaire de mise à jour du categorie
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie non trouvé avec l'ID : " + id));

        model.addAttribute("categorie", categorie);
        return "categorie/update";
    }

    // Endpoint pour traiter la mise à jour du categorie
    @PostMapping("/update/{id}")
    public String updateCategorie(@PathVariable("id") Long id, @ModelAttribute Categorie updatedCategorie) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie non trouvé avec l'ID : " + id));
        categorie.setNom(updatedCategorie.getNom());
        categorieRepository.save(categorie);
        return "redirect:/categories/";
    }

    // Supprimer un categorie
    @GetMapping("/delete/{id}")
    public String deleteCategorie(@PathVariable Long id) {
        categorieRepository.deleteById(id);
        return "redirect:/categories/";
    }
}
