package org.ornarowicz.refuseReport.service.refusalService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.ornarowicz.refuseReport.database.entity.SentReportsEntity;
import org.ornarowicz.refuseReport.database.repository.CrudReportsListRepository;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.ornarowicz.refuseReport.service.bo.SentReportsBO;
import org.ornarowicz.refuseReport.service.emailService.EmailService;
import org.ornarowicz.refuseReport.service.httpService.HttpClientPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ReportingService {
    @Autowired
    private CrudReportsListRepository reportsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpClientPost httpClientPost;

    @Getter(AccessLevel.PUBLIC)
    private boolean notWorking = true;

    public List<SentReportsBO> getReportsHistory() {
        var sentRejectionsBOList = new ArrayList<SentReportsBO>();
        sentRejectionsBOList.addAll(
                StreamSupport
                        .stream(reportsRepository
                                        .findAll()
                                        .spliterator()
                                , false
                        ).sorted(Comparator.comparing(
                        SentReportsEntity::getEmailId
                        , Comparator.reverseOrder()
                        )
                ).collect(Collectors.toList()));
        return sentRejectionsBOList;
    }

    private void saveSentRejections(List<SentReportsEntity> rejectionEntitiesP) {
        reportsRepository.saveAll(rejectionEntitiesP);
    }

    public void eraseSentRejections() {
        reportsRepository.deleteAll();
        emailService.clearAll();
    }

    public void sendReports() {
        this.notWorking = false;
        var lastEmailIdV = emailService.getLastEmailNum();
        var rejectionEntitiesC = new ArrayList<SentReportsEntity>();
        for (var msg : emailService.getEmails(false)) {
            var currentIdV = msg.getMessageId();
            if (currentIdV > lastEmailIdV) lastEmailIdV = currentIdV;
            for (var refusal : msg.getRefusalList()) {
                var isSentV = httpClientPost.sendForm(refusal);
                rejectionEntitiesC.add(sentReportsEntity(msg,refusal,isSentV));
                if (isSentV) {
                    log.info("Udało się wysłać formularz");
                    System.out.println(refusal);
                } else {
                    log.error("Coś nie działa podczas wysyłania formularza.");
                    System.err.println(refusal);
                }
            }
        }
        saveSentRejections(rejectionEntitiesC);
        emailService.discardOldMsgBOS(lastEmailIdV);
        this.notWorking = true;
    }

    private SentReportsEntity sentReportsEntity(MessageBO msgP, RefusalBO refusalP, boolean isSentP) {
        var entity = SentReportsEntity.builder()
                .emailId(msgP.getMessageId())
                .sentDate(new Date().getTime())
                .wholesalerName(refusalP.getWholesalerName())
                .wholesalerAddress(refusalP.getWholesalerAddress())
                .cureName(refusalP.getCureName())
                .cureDose(refusalP.getCureDose())
                .cureForm(refusalP.getCureForm())
                .curePack(refusalP.getCurePack())
                .orderedAmount(refusalP.getOrderedAmount())
                .receivedAmount(refusalP.getReceivedAmount())
                .refusalCause(refusalP.getRefusalCause())
                .isSent(isSentP)
                .build();
        return entity;
    }

}
