package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.CBREngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kerstin on 05/08/16.
 */
public class Case {

    private static HashMap<String, String> casecontent = new HashMap<String, String>();

    public Case(String caseID) {

        Project project = App.getProject();
        // create a concept and get the main concept of the project;
        de.dfki.mycbr.core.model.Concept myConcept = project.getConceptByID(CBREngine.getConceptName());
        Instance aInstance = myConcept.getInstance(caseID);

        HashMap<AttributeDesc, Attribute> atts = aInstance.getAttributes();
        HashMap<String, String> casecontent = new HashMap<String, String>();

        for (Map.Entry<AttributeDesc, Attribute> entry : atts.entrySet()) {
            AttributeDesc attDesc = entry.getKey();
            Attribute att = entry.getValue();
            String value = att.getValueAsString();
            value.trim();
            if (value.compareTo("_undefined_") != 0){
                if (value.compareTo("") != 0){
                    this.casecontent.put(attDesc.getName(), att.getValueAsString());
                }
            }
        }

    }

    public HashMap<String, String> getCase() {
        return casecontent;
    }
}
