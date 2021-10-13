package org.ornarowicz.refuseReport.service.httpService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.ornarowicz.refuseReport.service.configuration.HttpClientPostConfiguration;


public class HttpEntityBuilderTest {


    private static HttpClientPostConfiguration httpCfg;
    private HttpEntityBuilder sut;

    @BeforeAll
    public static void setUpAll(){
        httpCfg = new HttpClientPostConfiguration();
        httpCfg.setFormUrl("formUrl");
        httpCfg.setPhType("phType");
        httpCfg.setPhOwner("phOwner");
        httpCfg.setPhName("phName");
        httpCfg.setPhLocality("phLocality");
        httpCfg.setPhStreet("phStreet");
        httpCfg.setDrugType("drugType");
        httpCfg.setDrugDescription("drugDescription");
        httpCfg.setWhName("whName");
        httpCfg.setWhLocality("whLocality");
        httpCfg.setWhStreet("whStreat");
        httpCfg.setReason("reason");
        httpCfg.setDate("date");
        httpCfg.setWho("who");
        httpCfg.setRole("role");
    }

    @BeforeEach
    public void setUp(){
        sut = new HttpEntityBuilder(httpCfg);
    }

    @Test
    public void shouldReturn16Elements(){
        Assertions.assertEquals(16,sut.convert(new RefusalBO()).size());
    }
}