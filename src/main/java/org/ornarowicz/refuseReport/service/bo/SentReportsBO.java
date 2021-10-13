package org.ornarowicz.refuseReport.service.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SentReportsBO {
    private long
            emailId,
            sentDate;
    private String
            wholesalerName,
            wholesalerAddress,
            cureName,
            cureDose,
            cureForm,
            curePack,
            refusalCause;
    private int
            orderedAmount,
            receivedAmount;
    private boolean isSent;
}
