package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kerstin on 05/08/16.
 */
public class ConceptName {

    private Set<String> concepts;

    public ConceptName() {

        getConcept().clear();
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        concepts = project.getSubConcepts().keySet();
    }

    public Set<String> getConcept() {
        return concepts;
    }
}
