package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jon Tingvold on 22.03.2017.
 */

public class AmalgamationFunctions {

    private Concept concept = null;
    private final List<String> amalgamationFunctionIDs = new LinkedList<>();
    
    public AmalgamationFunctions(String conceptID) {
        concept = App.getProject().getConceptByID(conceptID);
        setAmalgamationFunctionIDs();
    }
    
    private void setAmalgamationFunctionIDs(){
	List<AmalgamationFct> amalgamationFunctions = concept.getAvailableAmalgamFcts();
        for (AmalgamationFct amalgamationFunction : amalgamationFunctions) {
            this.amalgamationFunctionIDs.add(amalgamationFunction.getName());
        }
    }
    
    public List<String> getAmalgamationFunctionIDs() {
        return amalgamationFunctionIDs;
    }
}