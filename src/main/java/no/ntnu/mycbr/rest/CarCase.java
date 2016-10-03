package no.ntnu.mycbr.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.SymbolDesc;

import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * Created by kerstin on 02/10/16.
 */

@Entity
public class CarCase {

    private final Instance sCase;
    private final Concept concept;


    CarCase(Instance sCase) {
        this.sCase = sCase;
        this.concept = sCase.getConcept();
    }

    public String getName() {
        return sCase.getName();
    }

    public String getBody() {
        SymbolDesc bodyDesc = (SymbolDesc) concept.getAllAttributeDescs().get("Body");
        return sCase.getAttributes().get(bodyDesc).getValueAsString();
    }

    public String getColor() {
        SymbolDesc colorDesc = (SymbolDesc) concept.getAllAttributeDescs().get("Color");
        return sCase.getAttributes().get(colorDesc).getValueAsString();
    }

   /* public String getGas() {
        return gas;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getCcm() {
        return ccm;
    }

    public int getDoors() {
        return doors;
    }

    public int getMildes() {
        return mildes;
    }

    public int getPower() {
        return power;
    }

    public int getPrice() {
        return price;
    }

    public int getSpeed() {
        return speed;
    }

    public int getYear() {
        return year;
    }

    public int getZip() {
        return zip;
    }*/
}
