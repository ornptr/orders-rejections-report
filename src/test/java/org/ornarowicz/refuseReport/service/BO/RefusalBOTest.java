package org.ornarowicz.refuseReport.service.BO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ornarowicz.refuseReport.service.bo.RefusalBO;

public class RefusalBOTest {

    private static RefusalBO sut;

    @BeforeEach
    public void setRefusal() {
        sut = new RefusalBO();
    }

    @Test
    void testToString() {
        sut.setWholesalerStreet("ulica");
        sut.setWholesalerName("nazwa");
        sut.setWholesalerLocality("miasto");
        sut.setWholesalerPostalCode("kodPocztowy");
        sut.setCureForm("tab.");
        sut.setRefusalCause("Nie mamy Pana leku i co nam Pan zrobi?");
        sut.setCurePack("*30");
        sut.setCureDose("końska");
        sut.setCureName("Lek");
        System.out.println(sut.toString());
    }

    @Test
    public void isGetStreetShowingCorrect() {
        sut.setWholesalerStreet("ul. RAKOWIECKA 65/67 50-422 WROCŁAW");
        Assertions.assertEquals("ul. RAKOWIECKA 65/67", sut.getWholesalerStreet());
    }


    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInRightOrder() {
        sut.setWholesalerStreet("ul. Makowa 6e");
        sut.setWholesalerLocality("Bolec");
        sut.setWholesalerPostalCode("65-001");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInWrongOrder1() {
        sut.setWholesalerPostalCode("65-001");
        sut.setWholesalerStreet("ul. Makowa 6e");
        sut.setWholesalerLocality("Bolec");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInWrongOrder2() {
        sut.setWholesalerPostalCode("65-001");
        sut.setWholesalerLocality("Bolec");
        sut.setWholesalerStreet("ul. Makowa 6e");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInWrongOrder3() {
        sut.setWholesalerLocality("Bolec");
        sut.setWholesalerPostalCode("65-001");
        sut.setWholesalerStreet("ul. Makowa 6e");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInWrongOrder4() {
        sut.setWholesalerLocality("Bolec");
        sut.setWholesalerStreet("ul. Makowa 6e");
        sut.setWholesalerPostalCode("65-001");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectAddressWhenAllAddressPartsAreSetInWrongOrder5() {
        sut.setWholesalerStreet("ul. Makowa 6e");
        sut.setWholesalerPostalCode("65-001");
        sut.setWholesalerLocality("Bolec");
        Assertions.assertEquals("65-001 Bolec ul. Makowa 6e", sut.getWholesalerAddress());
    }

    @Test
    public void shouldReturnCorrectPostalCodeAndLocalityWhenAreSetByStreet1() {
        sut.setWholesalerStreet("Makowa 6e 59-700 Bolec");
        Assertions.assertEquals("Bolec", sut.getWholesalerLocality());
        Assertions.assertEquals("59-700", sut.getWholesalerPostalCode());
    }

    @Test
    public void shouldReturnCorrectPostalCodeAndLocalityWhenAreSetByStreet2() {
        sut.setWholesalerStreet("65-tej Dywizji Pancernej Wojska Polskiego 123 A lok.14/17 65-411 Nakło nad Notecią");
        Assertions.assertEquals("Nakło nad Notecią", sut.getWholesalerLocality());
        Assertions.assertEquals("65-411", sut.getWholesalerPostalCode());
    }

    @Test
    public void shouldReturnCorrectPostalCodeAndLocalityWhenAreSetByStreet3() {
        sut.setWholesalerStreet("65-411 Nakło nad Notecią ul. 65-tej Dywizji Pancernej Wojska Polskiego 123 A lok.14/17");
        Assertions.assertEquals("Nakło nad Notecią", sut.getWholesalerLocality());
        Assertions.assertEquals("65-411", sut.getWholesalerPostalCode());
    }
}