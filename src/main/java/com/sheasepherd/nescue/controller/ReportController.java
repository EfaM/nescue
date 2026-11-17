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
    public String showReportForm(Model formModel) {
        formModel.addAttribute("reportForm", new ReportForm());
        formModel.addAttribute("sizeItems", Size.values());
        return "report";
    }

    @PostMapping("/report")
    public String submitReport(@Valid @ModelAttribute ReportForm reportForm, BindingResult result, Model formModel){
        if(result.hasErrors()){
            formModel.addAttribute("sizeItems", Size.values());
            return "report";
        }

        Coordinates coordinates = new Coordinates(reportForm.getLatitude(), reportForm.getLongitude());

        if (reportForm.getFirstname().isBlank() && reportForm.getLastname().isBlank() && reportForm.getPhoneNumber().isBlank()) {
            nescueService.reportNet(null, null, null, reportForm.getSize(), coordinates); // Anonyme Meldung
            return "redirect:/report?success";
        }

        if (reportForm.getFirstname().isEmpty() || reportForm.getLastname().isEmpty() || reportForm.getPhoneNumber().isEmpty()){ // Fehler
            return "redirect:/report?danger";
        }

        nescueService.reportNet(reportForm.getFirstname(), reportForm.getLastname(), reportForm.getPhoneNumber(), reportForm.getSize() , coordinates); // Meldung mit Kontaktdaten
        return "redirect:/report?success";
    }
}
