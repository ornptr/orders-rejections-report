package org.ornarowicz.refuseReport.service.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RefusalBO {
    private String
            wholesalerName = "",
            wholesalerLocality = "",
            wholesalerStreet = "",
            wholesalerPostalCode = "",
            cureName = "brak danych",
            cureDose = "",
            cureForm = "",
            curePack = "",
            refusalCause = "brak danych";

    private int
            orderedAmount,
            receivedAmount;


    public RefusalBO(RefusalBO refusalP) {
        this.wholesalerPostalCode = refusalP.wholesalerPostalCode;
        this.wholesalerName = refusalP.wholesalerName;
        this.wholesalerLocality = refusalP.wholesalerLocality;
        this.wholesalerStreet = refusalP.wholesalerStreet;
    }

    public String getWholesalerAddress() {
        return this.getWholesalerPostalCode() + " " + this.getWholesalerLocality() + " " + this.getWholesalerStreet();
    }

    public String getWholesalerStreet() {
        if (this.wholesalerLocality.isEmpty() && this.wholesalerPostalCode.isEmpty() && !this.wholesalerStreet.isEmpty()) {
            String street = this.wholesalerStreet;
            String[] tokens = street.split(" ");
            boolean isFirst = true;
            for (String token : tokens) {
                if (token.matches("[0-9]{2}-[0-9]{3}")) {
                    break;
                }
                street = isFirst ? token : street + " " + token;
                isFirst = false;
            }
            return street;
        }
        return this.wholesalerStreet;
    }

    public String getWholesalerLocality() {
        if (this.wholesalerLocality.isEmpty() && !this.wholesalerStreet.isEmpty()) {
            String[] tokens = this.wholesalerStreet.split(" ");
            String locality = "";
            for (int i = tokens.length - 1; i >= 0; i--) {
                if (tokens[i].matches("[0-9]{2}-[0-9]{3}")) { break; }
                if (locality.toLowerCase().startsWith("ul.")) {
                    locality = "";
                }
                locality = locality.isEmpty() ? tokens[i] : tokens[i] + " " + locality;
            }
            return locality;
        } else return this.wholesalerLocality;
    }

    public String getWholesalerPostalCode() {
        if (this.wholesalerPostalCode.isEmpty() && !this.wholesalerStreet.isEmpty()) {
            String[] tokens = this.wholesalerStreet.split(" ");
            for (String token : tokens) {
                if (token.matches("[0-9]{2}-[0-9]{3}")) {
                    return token;
                }
            }
        }
        return this.wholesalerPostalCode;
    }

    @Override
    public String toString() {
        return "Odmowa{" +
                "Nazwa hurtowni='" + getWholesalerName() + '\'' +
                ", Adres hurtowni='" + getWholesalerAddress() + '\'' +
                ", Nazwa leku='" + cureName + '\'' +
                ", Dawka='" + cureDose + '\'' +
                ", Postać='" + cureForm + '\'' +
                ", Opakowanie='" + curePack + '\'' +
                ", Przyczyna odmowy='" + refusalCause + '\'' +
                '}';
    }
}
