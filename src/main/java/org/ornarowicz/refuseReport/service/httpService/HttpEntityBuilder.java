package org.ornarowicz.refuseReport.service.httpService;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.ornarowicz.refuseReport.service.configuration.HttpClientPostConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HttpEntityBuilder {

    //    @Autowired
    private final HttpClientPostConfiguration httpCfg;

    @Autowired
    public HttpEntityBuilder(HttpClientPostConfiguration configuration) {
        this.httpCfg = configuration;
    }

    public String getFormUrl() {
        return httpCfg.getFormUrl();
    }

    public List<NameValuePair> convert(RefusalBO refusalP) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(httpCfg.getPhType(), toGoogleCharset("Apteka ogólnodostępna")));
        params.add(new BasicNameValuePair(httpCfg.getPhOwner(), toGoogleCharset("Owner Name")));
        params.add(new BasicNameValuePair(httpCfg.getPhName(), toGoogleCharset("Drugstore name")));
        params.add(new BasicNameValuePair(httpCfg.getPhLocality(), toGoogleCharset("Drustore place")));
        params.add(new BasicNameValuePair(httpCfg.getPhStreet(), toGoogleCharset("Drugstore street 123")));
        params.add(new BasicNameValuePair(httpCfg.getDrugType(), toGoogleCharset("produkt leczniczy ujęty w obwieszczeniu Ministra Zdrowia z dnia 6 listopada 2015 r. (Dz. Urz. MZ poz. 69)")));
        String drugDesc = refusalP.getCureName() + ", " + refusalP.getCureDose() + ", " + refusalP.getCureForm() + ", " + refusalP.getCurePack() + ", odmówiona ilość: " + (refusalP.getOrderedAmount() - refusalP.getReceivedAmount());
        params.add(new BasicNameValuePair(httpCfg.getDrugDescription(), toGoogleCharset(drugDesc)));
        params.add(new BasicNameValuePair(httpCfg.getWhName(), toGoogleCharset(refusalP.getWholesalerName())));
        params.add(new BasicNameValuePair(httpCfg.getWhLocality(), toGoogleCharset(refusalP.getWholesalerLocality())));
        params.add(new BasicNameValuePair(httpCfg.getWhStreet(), toGoogleCharset(refusalP.getWholesalerStreet())));
        params.add(new BasicNameValuePair(httpCfg.getReason(), toGoogleCharset(refusalP.getRefusalCause())));
        Date now = new Date();
        params.add(new BasicNameValuePair(httpCfg.getDate() + "_year", toGoogleCharset(new SimpleDateFormat("yyyy").format(now))));
        params.add(new BasicNameValuePair(httpCfg.getDate() + "_month", toGoogleCharset(new SimpleDateFormat("MM").format(now))));
        params.add(new BasicNameValuePair(httpCfg.getDate() + "_day", toGoogleCharset(new SimpleDateFormat("dd").format(now))));
        params.add(new BasicNameValuePair(httpCfg.getWho(), toGoogleCharset("Employee name")));
        params.add(new BasicNameValuePair(httpCfg.getRole(), toGoogleCharset("pracownik")));
        return params;
    }

    private String toGoogleCharset(String inputString) {
        return StandardCharsets.ISO_8859_1.decode(StandardCharsets.UTF_8.encode(inputString)).toString();
    }
}