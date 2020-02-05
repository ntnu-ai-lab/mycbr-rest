package no.ntnu.mycbr.rest.controller.helper;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.cbr.CBREngine;

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
        Concept concept = project.getConceptByID(CBREngine.getConceptName());
        Instance instance = concept.getInstance(caseID);
        if(instance != null)
            casecontent = new LinkedHashMap<>(getSortedCaseContent(instance));
        else
            casecontent = new LinkedHashMap<>();
    }

    public Case(String caseID, String conceptID) {

        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptID);
        Instance instance = concept.getInstance(caseID);
        
        casecontent.put(CASE_ID, instance.getName());
        casecontent.putAll(getSortedCaseContent(instance));
    }

    // Used by full results
    public Case(String conceptID, String caseID, double similarity) {

        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptID);
        Instance instance = concept.getInstance(caseID);

        casecontent.put(SIMILARITY, Double.toString(similarity));
        casecontent.put(CASE_ID, instance.getName());
        casecontent.putAll(getSortedCaseContent(instance));
    }

    public LinkedHashMap<String, String> getCase() {
        return casecontent;
    }
}
