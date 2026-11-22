package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.model.Coordinates;
import com.sheasepherd.nescue.model.Size;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportController {
    private final NescueService nescueService;

    public ReportController(NescueService nescueService){

        this.nescueService = nescueService;
    }

    @GetMapping("/report")
    public String showReportForm(Model model) {
        model.addAttribute("reportForm", new ReportForm());
        model.addAttribute("sizeItems", Size.values());
        return "report";
    }

    @PostMapping("/report")
    public String submitReport(@Valid @ModelAttribute ReportForm reportForm, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("sizeItems", Size.values());
            return "report";
        }

        Coordinates coordinates = new Coordinates(reportForm.getLatitude(), reportForm.getLongitude());
        nescueService.reportNet(reportForm.getFirstname(), reportForm.getLastname(), reportForm.getPhoneNumber(), reportForm.getSize() , coordinates);
        return "redirect:/report?success";
    }

}
