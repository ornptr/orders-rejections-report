package org.ornarowicz.refuseReport.service.emailService;

import lombok.extern.slf4j.Slf4j;
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
public class EmailFilter {

    @Autowired
    EmailConfiguration emailConfig;

    Message[] filterByDateAndKeywords(final Folder inboxP, final String[] keywordsP, final Date lastSentDateP) throws MessagingException {
        Message[] processingMessages;
        FetchProfile profile = new FetchProfile();
        profile.add(FetchProfile.Item.ENVELOPE);
        List<Message> matchedMsgsList = new ArrayList<>();
        int probeSize = emailConfig.getProbeSize();
        int start, end, chunk;
        for (int i = inboxP.getMessageCount(); i > 0; i -= probeSize) {
            if (i < probeSize && i > 1) {
                start = 1; end = i; chunk = i % probeSize;
            } else if (i > 1) {
                start = i - probeSize + 1; end = i; chunk = probeSize;
            } else {
                start = end = chunk = 1;
            }
            processingMessages = inboxP.getMessages(start, end);
            log.info("Messages range: {} - {}", start, end);
            inboxP.fetch(processingMessages, profile);
            boolean isFinished = matchConditions(chunk, processingMessages, lastSentDateP, keywordsP, matchedMsgsList);
            if (isFinished) {
                break;
            }
        }
        return matchedMsgsList.toArray(new Message[0]);
    }

    private boolean matchConditions(int chunkSizeP, Message[] messagesP, Date lastSentDateP, String[] keywordsP, List<Message> matchedMsgsOutP) throws MessagingException {
        for (int j = chunkSizeP - 1; j > -1; j--) {
            Date msgDate = messagesP[j].getReceivedDate();
            boolean finish = !lastSentDateP.before(msgDate);
            if (finish) {
                return true;
            } else {
                String subject = messagesP[j].getSubject();
                for (String keywordI : keywordsP) {
                    if (subject.toLowerCase().contains(keywordI)) {
                        matchedMsgsOutP.add(messagesP[j]);
                        break;
                    }
                }
            }
        }
        return false;
    }

}
