package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.CBREngine;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kerstin on 05/08/16.
 */
public class Case {

    private LinkedHashMap<String, String> casecontent = new LinkedHashMap<String, String>();

    public Case(String caseID) {

        Project project = App.getProject();
        de.dfki.mycbr.core.model.Concept myConcept = project.getConceptByID(CBREngine.getConceptName());
        Instance aInstance = myConcept.getInstance(caseID);
        casecontent = new LinkedHashMap<String, String>(getSortedCaseContent(aInstance));
    }

    // Used by full results
    public Case(String concept, String caseID, double similarity) {

        Project project = App.getProject();
        de.dfki.mycbr.core.model.Concept myConcept = project.getConceptByID(concept);
        Instance aInstance = myConcept.getInstance(caseID);

        casecontent.put("similarity", Double.toString(similarity));
        casecontent.put("caseID", aInstance.getName());
        casecontent.putAll(getSortedCaseContent(aInstance));
    }

    public LinkedHashMap<String, String> getCase() {
        return casecontent;
    }

    private static TreeMap<String, String> getSortedCaseContent(Instance aInstance) {
        HashMap<AttributeDesc, Attribute> atts = aInstance.getAttributes();
        TreeMap<String, String> sortedCaseContent = new TreeMap<String, String>();

        for(Map.Entry<AttributeDesc, Attribute> entry : atts.entrySet()) {
            AttributeDesc attDesc = entry.getKey();
            Attribute att = entry.getValue();
            String value = att.getValueAsString();
            value.trim();
            if (value.compareTo("_undefined_") != 0){
                if (value.compareTo("") != 0){
                    sortedCaseContent.put(attDesc.getName(), att.getValueAsString());
                }
            }
        }

        return sortedCaseContent;
    }
}
