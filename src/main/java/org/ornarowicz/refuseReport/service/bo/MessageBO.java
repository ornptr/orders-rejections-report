package org.ornarowicz.refuseReport.service.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class MessageBO {

    private final String
            subject,
            from;
    private final long messageId;
    private final List<RefusalBO> refusalList;
}
