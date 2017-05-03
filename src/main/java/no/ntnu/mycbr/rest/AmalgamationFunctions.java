package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.similarity.AmalgamationFct;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jon Tingvold on 22.03.2017.
 */
public class AmalgamationFunctions {

    private final List<String> amalgamationFunctions = new LinkedList<>();


    public AmalgamationFunctions(String conceptID) {

        de.dfki.mycbr.core.Project project = App.getProject();
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