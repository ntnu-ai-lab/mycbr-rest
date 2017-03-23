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

    private static LinkedHashMap<String, String> casecontent = new LinkedHashMap<String, String>();

    public Case(String caseID) {

        Project project = App.getProject();
        // create a concept and get the main concept of the project;
        de.dfki.mycbr.core.model.Concept myConcept = project.getConceptByID(CBREngine.getConceptName());
        Instance aInstance = myConcept.getInstance(caseID);

        HashMap<AttributeDesc, Attribute> atts = aInstance.getAttributes();
        TreeMap<String, String> sortedCaseContent = new TreeMap<String, String>();

        for (Map.Entry<AttributeDesc, Attribute> entry : atts.entrySet()) {
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

        casecontent = new LinkedHashMap<String, String>(sortedCaseContent);
    }

    public LinkedHashMap<String, String> getCase() {
        return casecontent;
    }
}
