package org.ornarowicz.refuseReport.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "LAST_ACCESS")
public class LastProcessedEmailEntity {

    @Id
    @Column(name = "ROW_ID")
    private final int rowId = 1;

    @Column(name = "LAST_NUM")
    private long lastNum;

}
