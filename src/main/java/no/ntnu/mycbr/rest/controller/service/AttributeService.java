package no.ntnu.mycbr.rest.controller.service;

import java.util.*;

import no.ntnu.mycbr.core.similarity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.model.DoubleDesc;
import no.ntnu.mycbr.core.model.FloatDesc;
import no.ntnu.mycbr.core.model.IntegerDesc;
import no.ntnu.mycbr.core.model.StringDesc;
import no.ntnu.mycbr.core.model.SymbolDesc;
import no.ntnu.mycbr.core.similarity.config.NumberConfig;
import no.ntnu.mycbr.rest.App;

/**
 * The AttributeService facilitates CRUD operations on attributes of a concept.
 *
 * @author Amar Jaiswal
 * @since 08 Jan 2020
 */
public class AttributeService {

    private static final String RANGE = "range";

    private final Log logger = LogFactory.getLog(getClass());

    private static final String STRING = "String";

    private Project project;
    private Concept concept;
    private AttributeDesc attributeDesc;

    public AttributeService() {
        project = App.getProject();
    }

    public AttributeService(String conceptName) {
        // Default constructor initialization
        this();
        concept = project.getConceptByID(conceptName);
    }

    public AttributeService(String conceptName, String attributeName) {
        //  Constructor initialization chain
        this(conceptName);
        attributeDesc = concept.getAttributeDesc(attributeName);
        logger.info("attributeDesc: " + attributeDesc.getName());
    }

    public boolean addDoubleSimilarityFunction(String conceptName, String attributeName, String similarityFunctionName, Double parameter) {
        Concept subConcept = (Concept) project.getSubConcepts().get(conceptName);
        DoubleDesc attributeDesc = (DoubleDesc) subConcept.getAllAttributeDescs().get(attributeName);
        //DoubleFct doubleFct = new DoubleFct(p,attributeDesc,similarityFunctionName);
        DoubleFct doubleFct = attributeDesc.addDoubleFct(similarityFunctionName, true);
        doubleFct.setSymmetric(true);
        doubleFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
        doubleFct.setFunctionParameterL(parameter);
        return true;
    }

    public boolean addIntegerSimilarityFunction(String conceptName, String attributeName, String similarityFunctionName) {
        Concept subConcept = (Concept) project.getSubConcepts().get(conceptName);
        IntegerDesc attributeDesc = (IntegerDesc) subConcept.getAllAttributeDescs().get(attributeName);

        for (ISimFct temp : attributeDesc.getSimFcts()) {
            logger.info("sim name: " + temp.getName());
        }


        IntegerFct intFct = attributeDesc.addIntegerFct(similarityFunctionName, true);

        logger.info("after adding: ");
        for (ISimFct temp : attributeDesc.getSimFcts()) {
            logger.info("sim name: " + temp.getName());
        }

        intFct.setSymmetric(true);
        intFct.setFunctionTypeL(NumberConfig.CONSTANT);
        return true;
    }

    public boolean deleteAllSimilarityFunctions() {
        concept.getActiveAmalgamFct().setActiveFct(attributeDesc, null);
        return true;
    }

    public Map<String, Object> getActiveSimilarityFunction() {

        Object object = concept.getActiveAmalgamFct().getActiveFct(attributeDesc);
        if (object instanceof SimFct) {
            return ((SimFct) object).getRepresentation();
        } else
            return null;
    }

    public LinkedList<String> getAllSimilarityFunctions() {
        IntegerDesc iAttDesc;
        SymbolDesc sAttDesc;
        FloatDesc fAttDesc;
        DoubleDesc dAttDesc;
        LinkedList<String> fctList= new LinkedList<String>();

            String name = attributeDesc.getName();
            logger.info("name: " + name);

            if (attributeDesc.getClass().getSimpleName().equals("IntegerDesc")){
                iAttDesc = (IntegerDesc) concept.getAttributeDesc(name);
                for (ISimFct aIntFct : iAttDesc.getSimFcts()) {
                    fctList.add(aIntFct.getName());
                    logger.info("fct: " + aIntFct.getName());
                }
            } else if (attributeDesc.getClass().getSimpleName().equals("DoubleDesc")) {
                dAttDesc = (DoubleDesc) concept.getAttributeDesc(name);
                for (ISimFct aDoubleFct : dAttDesc.getSimFcts()) {
                    fctList.add(aDoubleFct.getName());
                    logger.info("fct: " + aDoubleFct.getName());
                }
            } else if (attributeDesc.getClass().getSimpleName().equals("SymbolDesc")) {
                sAttDesc = (SymbolDesc) concept.getAttributeDesc(name);
                for (ISimFct aSymbolFct : sAttDesc.getSimFcts()) {
                    fctList.add(aSymbolFct.getName());
                    logger.info("fct: " + aSymbolFct.getName());
                }
            } else if (attributeDesc.getClass().getSimpleName().equals("FloatDesc")) {
                fAttDesc = (FloatDesc) concept.getAttributeDesc(name);
                for (ISimFct aFloatFct : fAttDesc.getSimFcts()) {
                    fctList.add(aFloatFct.getName());
                    logger.info("fct: " + aFloatFct.getName());
                }
            }
        return fctList;
    }

    public boolean deleteAttribute(String conceptID, String attributeID) {
        Concept subConcept = project.getSubConcepts().get(conceptID);

        try {
            subConcept.removeAttributeDesc(attributeID);
        } catch (Exception e) {
            logger.error("got an exception: ", e);
            return false;
        }

        project.save();

        return true;
    }

    /**
     * @param conceptID
     * @param attributeID
     * @param attributeJSON : e.g. "{"type": "Double","min": "0.0","max": "1.0"}"
     * @return
     */
    public boolean addAttribute(String conceptID, String attributeID, String attributeJSON) {
        Concept subConcept = project.getSubConcepts().get(conceptID);
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(attributeJSON);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String type = (String) json.get("type");
        String solution = (String) json.get("solution");
        try {
            if (type.contains(STRING)) {
                //This attribute registers with the concept through callback!
                addStringAttribute(subConcept, attributeID, solution.contentEquals("True"));
            } else if (type.contains("Double")) {
                if (json.containsKey("min") && json.containsKey("max")) {
                    double min = (Long) json.get("min");
                    double max = (Long) json.get("max");
                    //This attribute registers with the concept through callback!
                    attributeDesc = addDoubleAttribute(subConcept, attributeID, min, max, solution.contentEquals("True"));
                } else
                    return false;

            } else if (type.contains("Integer")) {
                if (json.containsKey("min") && json.containsKey("max")) {
                    int min = Integer.valueOf(String.valueOf((Long) json.get("min")));
                    int max = Integer.valueOf(String.valueOf((Long) json.get("max")));
                    //This attribute registers with the concept through callback!
                    attributeDesc = addIntegerAttribute(subConcept, attributeID, min, max, solution.contentEquals("True"));
                } else
                    return false;
            } else if (type.contains("Symbol")) {
                if (json.containsKey("allowedValues")) {
                    //This attribute registers with the concept through callback!
                    JSONArray arr = (JSONArray) json.get("allowedValues");
                    Set<String> allowedValues = new HashSet<String>();
                    for (Object o : arr) {
                        allowedValues.add((String) o);
                    }
                    SymbolDesc attributeDesc = new SymbolDesc(subConcept, attributeID, allowedValues);
                    if (solution.contentEquals("True"))
                        attributeDesc.setIsSolution(true);
                } else
                    return false;

            }
        } catch (Exception e) {
            logger.error("got an exception: ", e);
        }
        //project.save();
        return true;
    }

    /**
     * @param conceptID
     * @param attributeIDs : e.g. "att1,att2,att3"
     * @param attributeJSON : e.g. "{"type": "Integer","min": 0,"max": 1,"solution":"True"}"
     * @return
     */
    public boolean addAttributebyLists(String conceptID, String attributeIDs, String attributeJSON) {
        String[] attributeIDList = attributeIDs.split(",");
        for (int i=0; i<attributeIDList.length; i++)
        {
            String attributeID = attributeIDList[i];
            addAttribute(conceptID, attributeID, attributeJSON);
            logger.info(attributeID+" added");
        }
        return true;
    }
    /*
    public boolean copyAttribute(String conceptID, String attributeID, String newAttributeID) {
        logger.info("conceptID,  attributeID,  newAttributeID: " + conceptID +",  "+ attributeID +",  "+   newAttributeID);
        try {
            logger.info("concept name: " + project.getConceptByID(conceptID).getName());
            AttributeDesc existingAtt = project.getConceptByID(conceptID).getAttributeDesc(attributeID);
            AttributeDesc newAtt = new AttributeDesc();

            logger.info("project atts: " + project.getConceptByID(conceptID).getAllAttributeDescs().keySet());

            logger.info("newAtt name: " + newAtt.getName());
            logger.info("newAtt getRepresentation: " + newAtt.getRepresentation());

            newAtt.setName(newAttributeID);
            logger.info("new att name: " + newAtt.getName());
            //logger.info("old att name: " + project.getConceptByID(conceptID).getAttributeDesc(attributeID).getName());
            project.getConceptByID(conceptID).addAttributeDesc(newAtt);

            //String attributeJSON = getAttributeByID(conceptID, attributeID).toString();
            //addAttribute(conceptID,newAttributeID,attributeJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }*/

    private AttributeDesc addStringAttribute(Concept c, String attributeName, boolean solution) throws Exception {
        AttributeDesc attributeDesc = new StringDesc(c, attributeName);
        if (solution)
            attributeDesc.setIsSolution(true);
        return attributeDesc;
    }

    private AttributeDesc addDoubleAttribute(Concept c, String attributeName, double min, double max, boolean solution) throws Exception {
        AttributeDesc attributeDesc = new DoubleDesc(c, attributeName, min, max);
        if (solution)
            attributeDesc.setIsSolution(true);
        return attributeDesc;
    }

    private AttributeDesc addIntegerAttribute(Concept c, String attributeName, int min, int max, boolean solution) throws Exception {
        AttributeDesc attributeDesc = new IntegerDesc(c, attributeName, min, max);
        if (solution)
            attributeDesc.setIsSolution(true);
        return attributeDesc;
    }

    public boolean deleteAllAttribute(String conceptID) {
        Concept subConcept = project.getSubConcepts().get(conceptID);
        for (String attributeDescName : subConcept.getAllAttributeDescs().keySet()) {
            subConcept.removeAttributeDesc(attributeDescName);
        }
        return true;
    }

    public Map<String, Object> getAttributeByID(String conceptID, String attributeID) {
        Concept subConcept = project.getSubConcepts().get(conceptID);
        attributeDesc = subConcept.getAttributeDesc(attributeID);

        HashMap<String, AttributeDesc> allAttributeDescs = subConcept.getAllAttributeDescs();

        if (!allAttributeDescs.containsKey(attributeID))
            return null;

        Map<String, Object> map = allAttributeDescs.get(attributeID).getRepresentation();

        addRangeIfAbsent(map);

        return map;
    }

    public Map<String, String> getAllAttributes() {

        Map<String, String> map = new HashMap<String, String>();
        HashMap<String, AttributeDesc> attributeDescMap = concept.getAllAttributeDescs();

        for (Map.Entry<String, AttributeDesc> att : attributeDescMap.entrySet()) {
            String name = att.getKey();
            AttributeDesc attdesc = att.getValue();
            map.put(name, attdesc.getClass().getSimpleName());
        }

        return map;
    }

     public Map<String,Object> getAttributeDescription() {

	Map<String, Object> map = attributeDesc.getRepresentation();

	addRangeIfAbsent(map);

	return map;
    }

    private void addRangeIfAbsent(Map<String, Object> map) {
        if (!map.containsKey(RANGE)) {

            String attributeDescName = attributeDesc.getClass().getSimpleName();

            switch (attributeDescName) {
                case "DoubleDesc":
                    DoubleDesc doubleAttr = (DoubleDesc) attributeDesc;
                    Set<Double> doubleRange = new LinkedHashSet<Double>();
                    doubleRange.add(doubleAttr.getMin());
                    doubleRange.add(doubleAttr.getMax());
                    map.putIfAbsent(RANGE, doubleRange);
                    break;

                case "FloatDesc":
                    FloatDesc floatAttr = (FloatDesc) attributeDesc;
                    Set<Float> floatRange = new LinkedHashSet<Float>();
                    floatRange.add(floatAttr.getMin());
                    floatRange.add(floatAttr.getMax());
                    map.putIfAbsent(RANGE, floatRange);
                    break;

                case "IntegerDesc":
                    IntegerDesc integerAttr = (IntegerDesc) attributeDesc;
                    Set<Integer> integerRange = new LinkedHashSet<Integer>();
                    integerRange.add(integerAttr.getMin());
                    integerRange.add(integerAttr.getMax());
                    map.putIfAbsent(RANGE, integerRange);
                    break;

                case "SymbolDesc":
                    SymbolDesc symbolDesc = (SymbolDesc) attributeDesc;
                    map.putIfAbsent(RANGE, symbolDesc.getAllowedValues());
                    break;

                default:
                    logger.error("No matching AttributeDescription found for : " + attributeDescName);
                    map.putIfAbsent(RANGE, "n/a");
                    break;
            }
        }
    }
}
