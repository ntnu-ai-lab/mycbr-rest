package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kerstin on 05/08/16.
 */
public class ValueRange {

    private static final String ATTRIBUTE_TYPE = "attribute_type"; 
    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    public ValueRange(String concept, String attributeName) {

        Project project = App.getProject();
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);
        attributes.clear();

        AttributeDesc attdesc = myConcept.getAttributeDesc(attributeName);
        String AttributeDescName = attdesc.getClass().getSimpleName();
        if (AttributeDescName.equalsIgnoreCase("FloatDesc")){
            FloatDesc aFloatAtt = (FloatDesc) attdesc;
            Set<Float> range = new HashSet<>();
            range.add(aFloatAtt.getMin());
            range.add(aFloatAtt.getMax());
            attributes.put(attdesc.getName(),range);
            attributes.put(ATTRIBUTE_TYPE, AttributeDescName);
            
        }
        else if (AttributeDescName.equalsIgnoreCase("IntegerDesc")){
            IntegerDesc aIntegerAtt = (IntegerDesc) attdesc;
            Set<Integer> range = new HashSet<>();
            range.add(aIntegerAtt.getMin());
            range.add(aIntegerAtt.getMax());
            attributes.put(attdesc.getName(),range);
            attributes.put(ATTRIBUTE_TYPE, AttributeDescName);
        }
        
        else if (AttributeDescName.equalsIgnoreCase("DoubleDesc")){
            DoubleDesc aIntegerAtt = (DoubleDesc) attdesc;
            Set<Double> range = new HashSet<>();
            range.add(aIntegerAtt.getMin());
            range.add(aIntegerAtt.getMax());
            attributes.put(attdesc.getName(),range);
            attributes.put(ATTRIBUTE_TYPE, AttributeDescName);
        }
        else if (AttributeDescName.equalsIgnoreCase("SymbolDesc")){
            SymbolDesc aSymbolAtt = (SymbolDesc) attdesc;
            attributes.put(attdesc.getName(),aSymbolAtt.getAllowedValues());
            attributes.put(ATTRIBUTE_TYPE, AttributeDescName);
        }
        else {
            attributes.put(attdesc.getName(),"n/a");
            attributes.put(ATTRIBUTE_TYPE, AttributeDescName);
        }
    }

    public HashMap<String, Object> getValueRange() {
        return attributes;
    }

}
