package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.exception.NetNotFoundException;
import com.sheasepherd.nescue.model.Size;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class NetListController {
    private final NescueService nescueService;

    public NetListController(NescueService nescueService){

        this.nescueService = nescueService;
    }

    @GetMapping("/ghost-nets")
    public String showAssignForm(Model model) {
        model.addAttribute("nets", nescueService.getNetsList());
        model.addAttribute("sizeItemLetter", Size.values());
        model.addAttribute("assignForm", new AssignForm());
        return "/ghost-nets";
    }

    @PostMapping("/ghost-nets/{id}/assign")
    public String submitAssignment(@PathVariable UUID id, @Valid @ModelAttribute AssignForm assignForm, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("nets", nescueService.getNetsList());
            model.addAttribute("assignedNetId", id);
            return "ghost-nets";
        }

        try {
            nescueService.assignNet(assignForm.getFirstname(), assignForm.getLastname(), assignForm.getPhoneNumber(), id);
        }catch (NetNotFoundException e){
            return "redirect:/ghost-nets?netNotFound";
        }catch (IllegalStateException e){
            return "redirect:/ghost-nets?alreadyAssigned";
        }
        return "redirect:/ghost-nets?success";
    }

}