package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.model.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GhostNetsController {

    private final NescueService nescueService;

    public GhostNetsController(NescueService nescueService) {
        this.nescueService = nescueService;
    }

    @GetMapping("/ghost-nets")
    public String showGhostNets(Model model){
        model.addAttribute("nets", nescueService.getNetsList());
        model.addAttribute("sizeItemLetter", Size.values());
        return("ghost-nets");
    }

}