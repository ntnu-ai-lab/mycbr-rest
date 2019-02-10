package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.AttributeDesc;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
public class Attribute {

    private HashMap<String, String> attributes = new HashMap<String, String>();

    public Attribute(String concept) {

        Project project = App.getProject();
        // get a concept and get the main concept of the project;
        no.ntnu.mycbr.core.model.Concept myConcept = project.getConceptByID(concept);

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
