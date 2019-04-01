package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.CBREngine;
import no.ntnu.mycbr.core.model.Concept;

import java.util.LinkedHashMap;

import static no.ntnu.mycbr.rest.utils.Constants.*;
import static no.ntnu.mycbr.rest.utils.Helper.getSortedCaseContent;

/**
 * Created by kerstin on 05/08/16.
 */
public class Case {

    private LinkedHashMap<String, String> casecontent = new LinkedHashMap<String, String>();
    public Case(String caseID) {

        Project project = App.getProject();
        Concept myConcept = project.getConceptByID(CBREngine.getConceptName());
        Instance instance = myConcept.getInstance(caseID);
        if(instance != null)
            casecontent = new LinkedHashMap<>(getSortedCaseContent(instance));
        else
            casecontent = new LinkedHashMap<>();
    }

    public Case(String caseID, String conceptID) {

        Project project = App.getProject();
        Concept myConcept = project.getConceptByID(conceptID);
        Instance aInstance = myConcept.getInstance(caseID);
        casecontent = new LinkedHashMap<>(getSortedCaseContent(aInstance));
    }

    // Used by full results
    public Case(String concept, String caseID, double similarity) {

        Project project = App.getProject();
        Concept myConcept = project.getConceptByID(concept);
        Instance aInstance = myConcept.getInstance(caseID);

        casecontent.put(SIMILARITY, Double.toString(similarity));
        casecontent.put(CASE_ID, aInstance.getName());
        casecontent.putAll(getSortedCaseContent(aInstance));
    }

    public LinkedHashMap<String, String> getCase() {
        return casecontent;
    }
}
