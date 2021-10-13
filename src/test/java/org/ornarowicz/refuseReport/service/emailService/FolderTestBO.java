package org.ornarowicz.refuseReport.service.emailService;

import lombok.Getter;
import org.ornarowicz.refuseReport.service.bo.MessageBO;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FolderTestBO {

    List<MessageBO> messages = new ArrayList<>();
    public MessageBO[] getMessages(int P1, int P2) {
        List<MessageBO> fMsg = new ArrayList();
        for (int i = P1-1; i < P2; i++) {
            fMsg.add(getMessages().get(i));
        }
        return fMsg.toArray(new MessageBO[0]);
    }

    public MessageBO getMessage(int i) {
        return getMessages().get(i-1);
    }
}
