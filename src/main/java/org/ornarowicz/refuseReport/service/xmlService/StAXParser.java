package org.ornarowicz.refuseReport.service.xmlService;

import org.ornarowicz.refuseReport.service.bo.RefusalBO;
import org.springframework.stereotype.Component;
import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Component
public class StAXParser {

    public List<RefusalBO> parseXMLToRefusalBO(InputStream xmlStreamP) {

        List<RefusalBO> refusalBOList = new ArrayList<>();
        if (xmlStreamP == null) { return refusalBOList;}
        byte nrPozycji = -1;
        boolean bCzyDokumentToOdmowa = false, bOpisPrzyczynyOdmowy = false, bIloscZamowiona = false, bIloscOtrzymana = false;
        boolean bOdmawiajacy = false, bMiejsceDzialalnosci = false, bNazwaHurtowni = false, bUlicaHurtowni = false, bMiejscowoscHurtowni = false, bKodPocztowyHurtowni = false;
        boolean bNrBudHurtowni = false, bPozycja = false, bNazwaHandlowa = false, bMoc = false, bOpakowanie = false, bPostac = false, bPrzyczynaOdmowy = false;
        RefusalBO refusal = new RefusalBO();

        try {
            XMLStreamReader streamReader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStreamP);
            XMLEventReader eventReader = XMLInputFactory.newInstance().createXMLEventReader(streamReader);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                switch (event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase("odmowa")) {
                            bCzyDokumentToOdmowa = true;
                        } else if (qName.equalsIgnoreCase("odmawiajacy")) {
                            bOdmawiajacy = true;
                        } else if (qName.equalsIgnoreCase("miejsce_dzialalnosci") && bOdmawiajacy) {
                            bMiejsceDzialalnosci = true;
                        } else if (qName.equalsIgnoreCase("nazwa") && bOdmawiajacy && bMiejsceDzialalnosci) {
                            bNazwaHurtowni = true;
                        } else if (qName.equalsIgnoreCase("ulica") && bOdmawiajacy && bMiejsceDzialalnosci) {
                            bUlicaHurtowni = true;
                        } else if (qName.equalsIgnoreCase("nr_budynku") && bOdmawiajacy && bMiejsceDzialalnosci) {
                            bNrBudHurtowni = true;
                        } else if (qName.equalsIgnoreCase("miejscowosc") && bOdmawiajacy && bMiejsceDzialalnosci) {
                            bMiejscowoscHurtowni = true;
                        } else if (qName.equalsIgnoreCase("kod_pocztowy") && bOdmawiajacy && bMiejsceDzialalnosci) {
                            bKodPocztowyHurtowni = true;
                        } else if (qName.equalsIgnoreCase("pozycja")) {
                            bPozycja = true;
                            nrPozycji++;
                            if (nrPozycji > 0) {
                                refusal = new RefusalBO(refusal);
                            }
                        } else if (qName.equalsIgnoreCase("nazwa_handlowa") && bPozycja) {
                            bNazwaHandlowa = true;
                        } else if (qName.equalsIgnoreCase("moc")) {
                            bMoc = true;
                        } else if (qName.equalsIgnoreCase("opakowanie")) {
                            bOpakowanie = true;
                        } else if (qName.equalsIgnoreCase("postac")) {
                            bPostac = true;
                        } else if (qName.equalsIgnoreCase("ilosc_zamowiona")) {
                            bIloscZamowiona = true;
                        } else if (qName.equalsIgnoreCase("ilosc_otrzymana")) {
                            bIloscOtrzymana = true;
                        } else if (qName.equalsIgnoreCase("przyczyna_odmowy")) {
                            bPrzyczynaOdmowy = true;
                        } else if (qName.equalsIgnoreCase("opis_przyczyny_odmowy"))
                            bOpisPrzyczynyOdmowy = true;
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (bNazwaHurtowni) {
                            String compName = characters.getData();
                            String prevCompName = refusal.getWholesalerName();
                            boolean isPrevEmpty = prevCompName.equalsIgnoreCase("brak danych");
                            refusal.setWholesalerName(isPrevEmpty ? compName : prevCompName + compName);
                        }
                        if (bUlicaHurtowni) {
                            String ulica = characters.getData();
                            String present = refusal.getWholesalerStreet();
                            String prefix = present.isEmpty() ? "ul." : present;
                            ulica = prefix + " " + ulica;
                            refusal.setWholesalerStreet(ulica);
                        }
                        if (bNrBudHurtowni) {
                            String nrBud = characters.getData();
                            String present = refusal.getWholesalerStreet();
                            refusal.setWholesalerStreet(present + " " + nrBud);
                        }
                        if (bMiejscowoscHurtowni) {
                            String miejscowosc = characters.getData();
                            refusal.setWholesalerLocality(miejscowosc);
                        }
                        if (bKodPocztowyHurtowni) {
                            String kodPocz = characters.getData();
                            refusal.setWholesalerPostalCode(kodPocz);
                        }
                        if (bPozycja) {
                            if (bNazwaHandlowa) {
                                String name = characters.getData();
                                String prevName = refusal.getCureName();
                                refusal.setCureName(prevName.equalsIgnoreCase("brak danych") ? name : prevName + " " + name);
                            }
                            if (bMoc) {
                                String pow = characters.getData();
                                refusal.setCureDose(pow);
                            }
                            if (bOpakowanie) {
                                String pak = characters.getData();
                                refusal.setCurePack(pak);
                            }
                            if (bPostac) {
                                String form = characters.getData();
                                refusal.setCureForm(form);
                            }
                            if (bIloscZamowiona) {
                                try {
                                    int orderedAmount = Integer.parseInt(characters.getData());
                                    refusal.setOrderedAmount(orderedAmount);
                                } catch (NumberFormatException e) {
                                    System.err.println("Ordered amount in wrong format");
                                }
                            }
                            if (bIloscOtrzymana) {
                                try {
                                    int receivedAmount = Integer.parseInt(characters.getData());
                                    refusal.setReceivedAmount(receivedAmount);
                                } catch (NumberFormatException e) {
                                    System.err.println("Received amount in wrong format");
                                }
                            }
                            if (bPrzyczynaOdmowy) {
                                String reason = characters.getData();
                                refusal.setRefusalCause(reason);
                            }
                            if (bOpisPrzyczynyOdmowy) {
                                refusal.setRefusalCause(refusal.getRefusalCause() + ", " + characters.getData());
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        String localPart = endElement.getName().getLocalPart();

                        if (localPart.equalsIgnoreCase("odmawiajacy")) {
                            bOdmawiajacy = false;
                        } else if (localPart.equalsIgnoreCase("miejsce_dzialalnosci")) {
                            bMiejsceDzialalnosci = false;
                        } else if (localPart.equalsIgnoreCase("nazwa")) {
                            bNazwaHurtowni = false;
                        } else if (localPart.equalsIgnoreCase("ulica")) {
                            bUlicaHurtowni = false;
                        } else if (localPart.equalsIgnoreCase("nr_budynku")) {
                            bNrBudHurtowni = false;
                        } else if (localPart.equalsIgnoreCase("miejscowosc")) {
                            bMiejscowoscHurtowni = false;
                        } else if (localPart.equalsIgnoreCase("kod_pocztowy")) {
                            bKodPocztowyHurtowni = false;
                        } else if (localPart.equalsIgnoreCase("pozycja")) {
                            if (bCzyDokumentToOdmowa) refusalBOList.add(refusal);
                            bPozycja = false;
                        } else if (localPart.equalsIgnoreCase("nazwa_handlowa")) {
                            bNazwaHandlowa = false;
                        } else if (localPart.equalsIgnoreCase("moc")) {
                            bMoc = false;
                        } else if (localPart.equalsIgnoreCase("opakowanie")) {
                            bOpakowanie = false;
                        } else if (localPart.equalsIgnoreCase("postac")) {
                            bPostac = false;
                        } else if (localPart.equalsIgnoreCase("ilosc_zamowiona")) {
                            bIloscZamowiona = false;
                        } else if (localPart.equalsIgnoreCase("ilosc_otrzymana")) {
                            bIloscOtrzymana = false;
                        } else if (localPart.equalsIgnoreCase("przyczyna_odmowy")) {
                            bPrzyczynaOdmowy = false;
                        } else if (localPart.equalsIgnoreCase("opis_przyczyny_odmowy"))
                            bOpisPrzyczynyOdmowy = false;
                        break;
                }
            }
            eventReader.close();
            streamReader.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }


        return refusalBOList;
    }

}