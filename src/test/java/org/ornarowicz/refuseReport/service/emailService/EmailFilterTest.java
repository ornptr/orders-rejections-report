package org.ornarowicz.refuseReport.service.emailService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class EmailFilterTest {

    private static String[] keywords;

    private static FolderTestBO sut = new FolderTestBO();

    private static long now;

    private static int chunk = 20;

/*    public static void main(String[] args) {
        makeBOS();
    }*/

    @BeforeAll
    static void makeBOS() {
        keywords = new String[]{"odmow", "zorz", "test"};
        now = new Date().getTime()/1000;
        for (long i = (now - 7*24*3600),  j = 1; i <= now ; i = i + 6*3600, j++){
            sut.messages.add(MessageBO.builder()
                    .subject(keywords[(int) (j%3)])
                    .messageId(i*1000)
                    .refusalList(new ArrayList<>())
                    .build());
            System.out.println(new Date(i*1000) + "  " + sut.getMessages().get((int) (j - 1)).getSubject());
        }
        keywords[2] = "odmów";
        System.out.println("Size: "+ sut.getMessages().size());
    }

    @BeforeEach
    void resetKeyword() {
        keywords = new String[]{"odmow", "zorz", "odmów"};
    }

    @Test
    void shouldBe3() {
        Date last = new Date((now - 5*6*3600)*1000);
        MessageBO[] messageBOS = filterByDateAndKeywords(
                sut, keywords, last);
        for (MessageBO msg : messageBOS) {
            System.out.println(msg.getSubject()+"  "+msg.getMessageId()+"  "+new Date(msg.getMessageId()));
        }
        Assertions.assertEquals(3, messageBOS.length);
    }

    @Test
    void shouldBe0() {
        Date last = new Date((now - 5*6*3600)*1000);
        keywords = new String[]{"sd","tg","rtg"};
        MessageBO[] messageBOS = filterByDateAndKeywords(
                sut, keywords, last);
        Assertions.assertEquals(0, messageBOS.length);
    }

    //kopia 2-ch metod
    MessageBO[] filterByDateAndKeywords(final FolderTestBO inboxP, final String[] keywordsP, final Date lastSentDateP) {
        MessageBO[] processingMessages;
        int numOfMsgs = inboxP.getMessages().size();
        List<MessageBO> matchedMsgsList = new ArrayList<>();
        int probeSize = chunk;
        if (numOfMsgs > probeSize) {
            for (int i = numOfMsgs; i > 0; i -= probeSize) {
                if (i < probeSize && i > 1) {
                    processingMessages = inboxP.getMessages(1, i);
                    log.info("Messages range: 1 - {}", i);
                    boolean isFinished = matchConditions(i % probeSize, processingMessages, lastSentDateP, keywordsP, matchedMsgsList);
                    if (isFinished) {
                        break;
                    }
                } else if (i > 1) {
                    processingMessages = inboxP.getMessages(i - probeSize + 1, i);
                    log.info("Messages range: {} - {}", i - probeSize + 1, i);
                    boolean isFinished = matchConditions(probeSize, processingMessages, lastSentDateP, keywordsP, matchedMsgsList);
                    if (isFinished) {
                        break;
                    }
                } else {
                    processingMessages = new MessageBO[1];
                    processingMessages[0] = inboxP.getMessage(1);
                    boolean isFinished = matchConditions(1, processingMessages, lastSentDateP, keywordsP, matchedMsgsList);
                    if (isFinished) {
                        break;
                    }
                }
            }
        } else {
            processingMessages = inboxP.getMessages().toArray(new MessageBO[0]);
            matchConditions(numOfMsgs, processingMessages, lastSentDateP, keywordsP, matchedMsgsList);
        }
        return matchedMsgsList.toArray(new MessageBO[0]);
    }

    private boolean matchConditions(int chunkSizeP, MessageBO[] messagesP, Date lastSentDateP, String[] keywordsP, List<MessageBO> matchedMsgsOutP) {
        for (int j = chunkSizeP - 1; j > -1; j--) {
            Date msgDate = new Date(messagesP[j].getMessageId());
            boolean isBefore = lastSentDateP.before(msgDate);
            if (!isBefore) {
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
