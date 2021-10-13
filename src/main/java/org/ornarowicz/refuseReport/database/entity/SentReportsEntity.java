package org.ornarowicz.refuseReport.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ornarowicz.refuseReport.service.bo.SentReportsBO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "REJECTIONS")
public class SentReportsEntity extends SentReportsBO {

    @Id
    @Column(name = "sent_date")
    private long sentDate;
    @Column(name = "rej_id")
    private long emailId;
    @Column(name = "name")
    private String wholesalerName;
    @Column(name = "address")
    private String wholesalerAddress;
    @Column(name = "cure_name")
    private String cureName;
    @Column(name = "dose")
    private String cureDose;
    @Column(name = "form")
    private String cureForm;
    @Column(name = "pack")
    private String curePack;
    @Column(name = "ordered_amount")
    private int orderedAmount;
    @Column(name = "received_amount")
    private int receivedAmount;
    @Column(name = "r_Cause")
    private String refusalCause;
    @Column(name = "is_sent")
    private boolean isSent;
}
