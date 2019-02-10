package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jon Tingvold on 22.03.2017.
 */

public class AmalgamationFunctions {

    private final List<String> amalgamationFunctions = new LinkedList<>();


    public AmalgamationFunctions(String conceptID) {

        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptID);
        List<AmalgamationFct> amalgamationFunctions = concept.getAvailableAmalgamFcts();
        for (AmalgamationFct amalgamationFunction : amalgamationFunctions) {
            this.amalgamationFunctions.add(amalgamationFunction.getName());
        }
    }
    public List<String> getAmalgamationFunctions() {
        return amalgamationFunctions;
    }
}