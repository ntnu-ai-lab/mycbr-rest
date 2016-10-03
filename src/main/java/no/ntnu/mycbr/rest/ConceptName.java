package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import no.ntnu.mycbr.CBREngine;

/**
 * Created by kerstin on 05/08/16.
 */
public class ConceptName {

    private final String concept;

    private static DefaultCaseBase cb;
    private static de.dfki.mycbr.core.model.Concept myConcept;

    public ConceptName() {

        de.dfki.mycbr.core.Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        cb = (DefaultCaseBase)project.getCaseBases().get(CBREngine.getCaseBase());
        // create a concept and get the main concept of the project;
        myConcept = project.getConceptByID(CBREngine.getConceptName());
        this.concept = myConcept.getName();
    }

    public String getConcept() {
        return concept;
    }
}
