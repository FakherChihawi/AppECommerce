package com.example.appproduitsinfo.controller;
import com.example.appproduitsinfo.model.Categorie;
import com.example.appproduitsinfo.repository.CategorieRepository;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller // pour déclarer un contrôleur
@RequestMapping (value = "/") // nom logique dans l'URL pour accéder au contrôleur
public class HomeContoller {
    @RequestMapping (value = "/") // nom logique pour accéder à l'action "index" ou méthode "index
    public String index() {
        return "index";
    }
}
