package org.ornarowicz.refuseReport.service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "http-client-fields")
public class HttpClientPostConfiguration {
    String
            formUrl,
            phType,
            phOwner,
            phName,
            phLocality,
            phStreet,
            drugType,
            drugDescription,
            whName,
            whLocality,
            whStreet,
            reason,
            date,
            who,
            role;
}
