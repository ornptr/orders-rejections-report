package org.ornarowicz.refuseReport.controller.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
public class MessageDTO {
    private final String subject;
    private final String from;
    private final long messageId;
    private final Date receivedDate;
    private final int itemsNum;
}
