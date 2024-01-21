package com.example.appproduitsinfo.controller;

import com.example.appproduitsinfo.model.Client;
import com.example.appproduitsinfo.model.Facture;
import com.example.appproduitsinfo.model.Produit;
import com.example.appproduitsinfo.repository.ClientRepository;
import com.example.appproduitsinfo.repository.FactureRepository;
import com.example.appproduitsinfo.services.ClientServices;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping(value = "/clients")
public class ClientControlleur {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private FactureRepository factureRepository;

    @RequestMapping(value = "/")
    public String index(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "client/list";
    }

    @RequestMapping(value = "/plus_fideles")
    public String indexClientPlusFideles(Model model) {
        ClientServices clientServices = new ClientServices();
        List<Client> clients = clientServices.getClientsLesPlusFideles(clientRepository, factureRepository);
        model.addAttribute("clients", clients);
        return "client/list";
    }

    @RequestMapping(value = "/en_dette")
    public String ListeClientsEnDette(Model model) {
        List<Client> clients = clientRepository.findAll();
        List<Client> clientsEnDette = new ArrayList<>();
        for (Client client : clients){
            List<Facture> factures = factureRepository.findAllByClient(client);
            for (Facture facture : factures){
                if (!facture.isEstPayee() && !facture.getReglements().isEmpty()){
                    clientsEnDette.add(client);
                    break;
                }
            }
        }
        model.addAttribute("clients", clientsEnDette);
        return "client/clientsEnDettes";
    }

    @RequestMapping(value = "/new")
    public String newClient() {
        return "client/new";
    }

    @PostMapping("/save")
    public String saveClient(HttpServletRequest request) {
        String nom = request.getParameter("nom");
        String adresse = request.getParameter("adresse");
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");

        Client client = new Client();
        client.setNom(nom);
        client.setAdresse(adresse);
        client.setTel(tel);
        client.setEmail(email);

        clientRepository.save(client);
        return "redirect:/clients/";
    }

    // Endpoint pour afficher le formulaire de mise à jour du client
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + id));

        model.addAttribute("client", client);
        return "client/update";
    }

    // Endpoint pour traiter la mise à jour du client
    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") Long id, @ModelAttribute Client updatedClient) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + id));

        client.setNom(updatedClient.getNom());
        client.setEmail(updatedClient.getEmail());
        client.setTel(updatedClient.getTel());
        client.setAdresse(updatedClient.getAdresse());

        clientRepository.save(client);
        return "redirect:/clients/";
    }


    // Supprimer un client
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return "redirect:/clients/";
    }


    @GetMapping(value = "/details/{id}")
    public String afficherDetails(@PathVariable("id") Long id,Model model) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID : " + id));
        ClientServices clientServices = new ClientServices();
        Map<Integer, Double> chiffreAffairesParAnnee = clientServices.getAffairesParClientEtAnnee(client, factureRepository);
        Double chiffreAffairesGlobal = clientServices.getChiffreAffaireGlobal(client, factureRepository);
        Double resteGlobaleNonPayee = clientServices.getResteGlobaleNonPayee(client, factureRepository);
        List<Facture> facturesReglees = clientServices.getFactureRegleee(client, factureRepository);
        List<Facture> facturesNonReglees = clientServices.getFactureNonRegleee(client,factureRepository);
        List<Produit> produitsLesPlusSollicites = clientServices.getProduitsLesPlusSollicites(client, factureRepository);

        model.addAttribute("client", client);
        model.addAttribute("chiffreAffairesParAnnee", chiffreAffairesParAnnee);
        model.addAttribute("chiffreAffairesGlobal", chiffreAffairesGlobal);
        model.addAttribute("resteGlobaleNonPayee", resteGlobaleNonPayee);
        model.addAttribute("facturesReglees", facturesReglees);
        model.addAttribute("facturesNonReglees", facturesNonReglees);
        model.addAttribute("produitsLesPlusSollicites", produitsLesPlusSollicites);
        return "client/detail";
    }
}
