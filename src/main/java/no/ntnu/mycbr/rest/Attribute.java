package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.CBREngine;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
public class Attribute {

    private static HashMap<String, String> attributes = new HashMap<String, String>();

    public Attribute(String concept) {

        Project project = App.getProject();
        // create a concept and get the main concept of the project;
        de.dfki.mycbr.core.model.Concept myConcept = project.getConceptByID(concept);

        HashMap<String, AttributeDesc> atts = myConcept.getAllAttributeDescs();
        for (Map.Entry<String, AttributeDesc> att : atts.entrySet()) {
            String name = att.getKey();
            AttributeDesc attdesc = att.getValue();
            attributes.put(name, attdesc.getClass().getSimpleName());
        }

    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

}
