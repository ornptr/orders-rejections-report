package org.ornarowicz.refuseReport.service.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "email-config")
public class EmailConfiguration {
    private String
            protocol,
            host,
            port,
            userName,
            password,
            attachmentExtension,
            inbox;
    private String[] searchKeywords;
    private int
            probeSize,
            numOfDaysBack;
}
