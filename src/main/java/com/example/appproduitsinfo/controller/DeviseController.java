package com.example.appproduitsinfo.controller;
import com.example.appproduitsinfo.model.Devise;
import com.example.appproduitsinfo.repository.DeviseRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping (value = "/devises")
public class DeviseController {
    @Autowired
    private DeviseRepository deviseRepository;

    @RequestMapping (value = "/")
    public String index(Model model) {
        List<Devise> devises = deviseRepository.findAll();
        model.addAttribute("devises", devises);
        return "devise/list";
    }
    @RequestMapping (value = "/new")
    public String newDevise ()
    {
        return "devise/new";
    }

    @PostMapping("/save")
    public String saveDevises(HttpServletRequest request) {
        String nom = request.getParameter("nom");
        Devise devise = new Devise(nom);
        deviseRepository.save(devise);
        return "redirect:/devises/";
    }


    // Endpoint pour afficher le formulaire de mise à jour du devise
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Devise devise = deviseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Devise non trouvé avec l'ID : " + id));

        model.addAttribute("devise", devise);
        return "devise/update";
    }

    // Endpoint pour traiter la mise à jour du devise
    @PostMapping("/update/{id}")
    public String updateDevise(@PathVariable("id") Long id, HttpServletRequest request) {
        Devise devise = deviseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Devise non trouvé avec l'ID : " + id));

        String nom = request.getParameter("nom");
        devise.setNom(nom);

        deviseRepository.save(devise);
        return "redirect:/devises/";
    }

    // Supprimer un devise
    @GetMapping("/delete/{id}")
    public String deleteDevise(@PathVariable Long id) {
        deviseRepository.deleteById(id);
        return "redirect:/devises/";
    }

}
