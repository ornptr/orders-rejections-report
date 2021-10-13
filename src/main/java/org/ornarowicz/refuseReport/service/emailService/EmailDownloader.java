package org.ornarowicz.refuseReport.service.emailService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.configuration.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class EmailDownloader {

    @Autowired
    EmailConnection emailConnection;

    @Autowired
    private EmailConfiguration emailConfig;

    @Autowired
    private EmailFilter emailFilter;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AttachmentToMessageBOConverter attachmentConverter;

    @Getter(AccessLevel.PUBLIC)
    private boolean notWorking = true;

    MessageBO[] getRefusalsMsgs() {
        this.notWorking = false;
        Folder inboxV = null;
        List<MessageBO> emailsWithRejections = new ArrayList<>();
        try {
            inboxV = emailConnection.connect();
            Message[] foundMessages = emailFilter.filterByDateAndKeywords(
                    inboxV
                    , emailConfig.getSearchKeywords()
                    , new Date(emailService.getLastEmailNum()));
            log.info("Przeszukiwanie emaili zakończono. Czytam załączniki i tworzę obiekty\n");
            FetchProfile profile = new FetchProfile();
            profile.add(FetchProfile.Item.CONTENT_INFO);
            inboxV.fetch(foundMessages, profile);
            emailsWithRejections = attachmentConverter.createRefusalMsgsBOList(foundMessages);
            log.info("Tworzenie obiektów zakończono.");
        } catch (MessagingException e) {
            log.error("Błąd podczas pobierania wiadomości!!!");
            e.printStackTrace();
        } finally {
            try {
                emailConnection.disconnect(inboxV);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        this.notWorking = true;
        return emailsWithRejections.toArray(new MessageBO[0]);
    }

}
