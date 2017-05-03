package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.model.Concept;
import no.ntnu.mycbr.CBREngine;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kerstin on 05/08/16.
 */
public class ConceptName {

    private Set concepts = new HashSet();

    public ConceptName() {

        concepts.clear();
        de.dfki.mycbr.core.Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        concepts = project.getSubConcepts().keySet();
    }

    public Set getConcept() {
        return concepts;
    }
}
