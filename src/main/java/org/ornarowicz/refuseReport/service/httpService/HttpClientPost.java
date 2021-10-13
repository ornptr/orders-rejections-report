package org.ornarowicz.refuseReport.service.httpService;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HttpClientPost {
    @Autowired
    private HttpEntityBuilder builder;

    public boolean sendForm(RefusalBO rejectionP) {
        CloseableHttpClient client = null;
        int status = 0;
        try {
            client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(builder.getFormUrl());
            httpPost.setEntity(new UrlEncodedFormEntity(builder.convert(rejectionP)));
            CloseableHttpResponse response = client.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
            log.info("Status HTTP wysyłania formularza: {}", status);
            if (status != 200) {
                log.error(response.getStatusLine().toString());
            }
        } catch (IOException e) {
            log.error("Sending error!!!: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }
        return status == 200;
    }
}