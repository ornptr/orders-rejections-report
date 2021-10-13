package org.ornarowicz.refuseReport.service.emailService;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.ornarowicz.refuseReport.service.configuration.EmailConfiguration;
import org.ornarowicz.refuseReport.service.xmlService.StAXParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AttachmentToMessageBOConverter {

    @Autowired
    private EmailConfiguration emailConfig;

    @Autowired
    private StAXParser xmlParser;


    List<MessageBO> createRefusalMsgsBOList(Message[] messagesP) throws MessagingException {
        var emailsWithXmlRefusals = new ArrayList<MessageBO>();
        try {
            for (Message msg : messagesP) {
                var attInputStream = getAttInStream(msg);
                var refusalBOS = xmlParser.parseXMLToRefusalBO(attInputStream);
                if (attInputStream != null) { attInputStream.close(); }
                if (!refusalBOS.isEmpty()) {
                    emailsWithXmlRefusals.add(createMessageBO(msg, refusalBOS));
                }
            }
        } catch (IOException | NullPointerException e) {
            log.error("Problem z odczytem załącznika");
            e.printStackTrace();
        }
        return emailsWithXmlRefusals;
    }

    private InputStream getAttInStream(Message msgP) throws MessagingException, IOException {
        var extension = emailConfig.getAttachmentExtension();
        if (msgP.getContentType().contains("multipart")) {
            var contentBody = (MimeMultipart) msgP.getContent();
            for (int i = 0; i < contentBody.getCount(); i++) {
                var part = (MimeBodyPart) contentBody.getBodyPart(i);
                var AttFileName = part.getFileName();
                if (AttFileName != null && AttFileName.toLowerCase().endsWith("." + extension)) {
                    return part.getInputStream();
                }
            }
        } else {
            log.error(extension.toUpperCase() + " attachment not found!!!");
        }
        return null;
    }

    private MessageBO createMessageBO(Message msgP, List<RefusalBO> refusalsP) throws MessagingException {
        var msgBO = MessageBO.builder()
                .subject(msgP.getSubject())
                .from(msgP.getFrom()[0].toString())
                .messageId(msgP.getReceivedDate().getTime())
                .refusalList(refusalsP)
                .build();
        System.out.println(
                "Message ID: " + msgBO.getMessageId() + " | " +
                        "Date: " + new Date(msgBO.getMessageId()) +
                        " | From: " + msgBO.getFrom() +
                        " | Subject: " + msgBO.getSubject() +
//                        "\nRefusal content:\n" + msgBO.getRefusalList() +
                        "\n----------------------------------------");
        return msgBO;
    }
}
