package org.ornarowicz.refuseReport.controller.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@ToString
public class RefusalDTO {
    private final Date receivedDate;
    private final Date sentDate;
    private final String fileName;
    private final String wholesalerName;
    private final String wholesalerAddress;
    private final String cureName;
    private final String cureDose;
    private final String cureForm;
    private final String curePack;
    private final int orderedAmount;
    private final int receivedAmount;
    private final String refusalCause;
    private final Boolean isSent;
}
