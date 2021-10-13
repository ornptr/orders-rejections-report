package org.ornarowicz.refuseReport.controller;

import org.ornarowicz.refuseReport.controller.BOtoDTOmappers.BOToDTOMappers;
import org.ornarowicz.refuseReport.controller.DTO.RefusalDTO;
import org.ornarowicz.refuseReport.service.refusalService.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;


@Controller
public class MainPageController {

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private BOToDTOMappers toDTO;

    @GetMapping("/")
    public String getRefusalHistory(Model model) {
        model.addAttribute("historyList", toDTO.sentReportsBOListToDTO(reportingService.getReportsHistory()));
        return "mainPage";
    }

    @GetMapping("/reset")
    public String resetAll() {
        reportingService.eraseSentRejections();
        return "redirect:/";
    }

}
