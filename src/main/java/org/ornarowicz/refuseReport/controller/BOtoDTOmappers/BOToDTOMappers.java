package org.ornarowicz.refuseReport.controller.BOtoDTOmappers;

import org.ornarowicz.refuseReport.controller.DTO.MessageDTO;
import org.ornarowicz.refuseReport.controller.DTO.RefusalDTO;
import org.ornarowicz.refuseReport.service.bo.MessageBO;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.ornarowicz.refuseReport.service.bo.SentReportsBO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BOToDTOMappers {

    public List<MessageDTO> messageBOListToDTO(MessageBO[] msgBOListP) {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        for (MessageBO msgBO : msgBOListP) {
            MessageDTO msgDTO = MessageDTO.builder()
                    .subject(msgBO.getSubject())
                    .from(msgBO.getFrom())
                    .messageId(msgBO.getMessageId())
                    .receivedDate(new Date(msgBO.getMessageId()))
                    .itemsNum(msgBO.getRefusalList().size())
                    .build();
            messageDTOS.add(msgDTO);
        }
        return messageDTOS;
    }


    public List<RefusalDTO> sentReportsBOListToDTO(List<SentReportsBO> rejBOListP) {
        List<RefusalDTO> historyList = new ArrayList<>();
        for (SentReportsBO rejectionBO : rejBOListP) {
            RefusalDTO refusalDTO = RefusalDTO.builder()
                    .receivedDate(new Date(rejectionBO.getEmailId()))
                    .sentDate(new Date(rejectionBO.getSentDate()))
                    .wholesalerName(rejectionBO.getWholesalerName())
                    .wholesalerAddress(rejectionBO.getWholesalerAddress())
                    .cureName(rejectionBO.getCureName())
                    .cureDose(rejectionBO.getCureDose())
                    .cureForm(rejectionBO.getCureForm())
                    .curePack(rejectionBO.getCurePack())
                    .orderedAmount(rejectionBO.getOrderedAmount())
                    .receivedAmount(rejectionBO.getReceivedAmount())
                    .refusalCause(rejectionBO.getRefusalCause())
                    .isSent(rejectionBO.isSent())
                    .build();

            historyList.add(refusalDTO);
        }
        return historyList;
    }

    public List<RefusalDTO> RefusalListBOToDTO(List<RefusalBO> refBOListP, long messageIdP) {
        List<RefusalDTO> refusalDTOList = new ArrayList<>();
        for (RefusalBO refusalBO : refBOListP) {
        RefusalDTO refusalDTO = RefusalDTO.builder()
                .receivedDate(new Date(messageIdP))
                .wholesalerName(refusalBO.getWholesalerName())
                .wholesalerAddress(refusalBO.getWholesalerAddress())
                .cureName(refusalBO.getCureName())
                .cureDose(refusalBO.getCureDose())
                .cureForm(refusalBO.getCureForm())
                .curePack(refusalBO.getCurePack())
                .orderedAmount(refusalBO.getOrderedAmount())
                .receivedAmount(refusalBO.getReceivedAmount())
                .refusalCause(refusalBO.getRefusalCause())
                .build();
        refusalDTOList.add(refusalDTO);
        }
        return refusalDTOList;
    }


}
