package fr.eql.courseplanner.controller;

import fr.eql.courseplanner.entities.Promo;
import fr.eql.courseplanner.services.IPromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SchoolController {

    @Autowired
    private IPromoService promoService;

    @GetMapping("/showPromos")
    public String findPromos(Model model){

        List<Promo> promos = promoService.findAll();

        model.addAttribute("promos", promos);

        return  "showPromos";

    }
}
