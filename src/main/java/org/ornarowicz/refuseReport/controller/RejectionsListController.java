package org.ornarowicz.refuseReport.controller;

import org.ornarowicz.refuseReport.controller.BOtoDTOmappers.BOToDTOMappers;
import org.ornarowicz.refuseReport.controller.DTO.RefusalDTO;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.emailService.EmailService;
import org.ornarowicz.refuseReport.service.refusalService.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class RejectionsListController {

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private BOToDTOMappers toDTO;

    @Autowired
    private EmailService emailService;


    @GetMapping("/refusalList")
    public String getFilesList(Model model) {
        List<RefusalDTO> refList = new ArrayList<>();
        for (MessageBO messageBO : emailService.getEmails(false)) {
            refList.addAll(toDTO.RefusalListBOToDTO(messageBO.getRefusalList(), messageBO.getMessageId()));
        }
        model.addAttribute("refusalList", refList);
        return "refusalList";
    }

    @GetMapping("/sendForms")
    public String sendFormFromXmls() {
        if (!reportingService.isNotWorking()) {
            return "working";
        }
        reportingService.sendReports();
        return "redirect:/";
    }

}
