package org.ornarowicz.refuseReport.controller;

import org.ornarowicz.refuseReport.controller.BOtoDTOmappers.BOToDTOMappers;
import org.ornarowicz.refuseReport.controller.DTO.MessageDTO;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.emailService.EmailDownloader;
import org.ornarowicz.refuseReport.service.emailService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmailsListController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private BOToDTOMappers toDTO;

    @Autowired
    private EmailDownloader downloader;

    @GetMapping("/emails")
    public String emails(Model model) {
        if (downloader.isNotWorking()) {
            MessageBO[] emails = emailService.getEmails(false);
            model.addAttribute("messagesList", toDTO.messageBOListToDTO(emails));
            return "emailsList";
        } else return "working";
    }

    @GetMapping("/refreshEmails")
    public String refreshEmails() {
        if (downloader.isNotWorking()) {
            emailService.getEmails(true);
        }
        return "redirect:/emails";
    }

    @GetMapping("/markLast")
    public String markLast(@RequestParam(value = "messageId") long idP) {
        emailService.discardOldMsgBOS(idP);
        return "redirect:/emails";
    }
}
