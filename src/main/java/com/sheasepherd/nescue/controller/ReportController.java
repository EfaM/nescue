package com.sheasepherd.nescue.controller;

import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.model.Coordinates;
import com.sheasepherd.nescue.model.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String submitReport(@ModelAttribute ReportForm reportForm){
        Coordinates coordinates = new Coordinates(reportForm.getLatitude(), reportForm.getLongitude());
        nescueService.reportNet(null, null,null, reportForm.getSize(),coordinates); // anonyme Meldung
        return "redirect:/report?success";
    }
}
