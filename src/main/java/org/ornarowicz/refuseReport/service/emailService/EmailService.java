package org.ornarowicz.refuseReport.service.emailService;

import lombok.extern.slf4j.Slf4j;
import org.ornarowicz.refuseReport.database.entity.LastProcessedEmailEntity;
import org.ornarowicz.refuseReport.database.repository.CrudNumOfLastEmailRepository;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.configuration.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private CrudNumOfLastEmailRepository crudLastEmail;

    @Autowired
    private EmailDownloader emailDownloader;

    @Autowired
    private  EmailConfiguration emailConfig;

    private MessageBO[] messageBOS;


    public void clearAll() {
        this.messageBOS = null;
        crudLastEmail.deleteAll();
    }

     public long getLastEmailNum() {
        return crudLastEmail.findById(1).map(LastProcessedEmailEntity::getLastNum)
                .orElseGet(() -> new Date().getTime() - (long) emailConfig.getNumOfDaysBack() * 1000 * 3600 * 24);
    }

    private void setLastEmailNum(long lastIdP) {
        LastProcessedEmailEntity lastProcessedEmailEntity = crudLastEmail.findById(1)
                .orElseGet(LastProcessedEmailEntity::new);
        lastProcessedEmailEntity.setLastNum(lastIdP);
        crudLastEmail.save(lastProcessedEmailEntity);
    }

    public void discardOldMsgBOS(long lastIdToDelP) {
        this.messageBOS = Arrays.stream(this.messageBOS)
                .filter(msg -> msg.getMessageId() > lastIdToDelP)
                .toArray(MessageBO[]::new);
        setLastEmailNum(lastIdToDelP);
    }

    public MessageBO[] getEmails(boolean refreshP) {
        if (this.messageBOS == null || refreshP) {
            refreshEmails();
        }
        return this.messageBOS;
    }

    public void refreshEmails() {
        boolean isDone = false;
        if (emailDownloader.isNotWorking()) {
            this.messageBOS = emailDownloader.getRefusalsMsgs();
        }
    }

}//class EmailService
