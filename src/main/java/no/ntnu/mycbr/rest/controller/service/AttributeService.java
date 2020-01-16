package no.ntnu.mycbr.rest.controller.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
import no.ntnu.mycbr.core.similarity.DoubleFct;
import no.ntnu.mycbr.core.similarity.SimFct;
import no.ntnu.mycbr.core.similarity.config.NumberConfig;
import no.ntnu.mycbr.rest.App;

/**
 * The AttributeService facilitates CRUD operations on attributes of a concept.
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
    }

    public boolean addDoubleSimilarityFunction(String conceptName, String attributeName, String similarityFunctionName, Double parameter){
	Concept subConcept = (Concept)project.getSubConcepts().get(conceptName);
	DoubleDesc attributeDesc = (DoubleDesc) subConcept.getAllAttributeDescs().get(attributeName);
	//DoubleFct doubleFct = new DoubleFct(p,attributeDesc,similarityFunctionName);
	DoubleFct doubleFct = attributeDesc.addDoubleFct(similarityFunctionName,true);
	doubleFct.setSymmetric(true);
	doubleFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
	doubleFct.setFunctionParameterL(parameter);
	return true;
    }

    public boolean deleteAllSimilarityFunctions() {
	concept.getActiveAmalgamFct().setActiveFct(attributeDesc, null);
	return true;
    }

    public Map<String, Object> getAllSimilarityFunctions() {

	Object object = concept.getActiveAmalgamFct().getActiveFct(attributeDesc);
	if(object instanceof  SimFct){
	    return  ( (SimFct)object ).getRepresentation();
	}else
	    return null;
    }


    public boolean deleteAttribute(String conceptID, String attributeID) {
	Concept subConcept = project.getSubConcepts().get(conceptID);

	try {
	    subConcept.removeAttributeDesc(attributeID);
	}catch (Exception e){
	    logger.error("got an exception: ",e);
	    return false;
	}

	project.save();

	return true;
    }

    /**
     * 
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
	    } else if(type.contains("Double")){
		if(json.containsKey("min") && json.containsKey("max")) {
		    double min = (Double)json.get("min");
		    double max = (Double)json.get("max");
		    //This attribute registers with the concept through callback!
		    attributeDesc = addDoubleAttribute(subConcept, attributeID, min, max, solution.contentEquals("True"));
		}else
		    return false;

	    } else if(type.contains("Symbol")){
		if(json.containsKey("allowedValues")) {
		    //This attribute registers with the concept through callback!
		    JSONArray arr = (JSONArray) json.get("allowedValues");
		    Set<String> allowedValues = new HashSet<String>();
		    for(Object o : arr){
			allowedValues.add((String)o);
		    }
		    SymbolDesc attributeDesc = new SymbolDesc( subConcept, attributeID, allowedValues);
		    if(solution.contentEquals("True"))
			attributeDesc.setIsSolution(true);
		}else
		    return false;

	    }
	}catch (Exception e){
	    logger.error("got an exception: ",e);
	}
	project.save();
	return true;
    }

    private AttributeDesc addStringAttribute(Concept c, String attributeName, boolean solution) throws Exception {
	AttributeDesc attributeDesc = new StringDesc(c, attributeName);
	if(solution)
	    attributeDesc.setIsSolution(true);
	return attributeDesc;
    }

    private AttributeDesc addDoubleAttribute(Concept c, String attributeName, double min, double max, boolean solution) throws Exception {
	AttributeDesc attributeDesc = new DoubleDesc(c, attributeName, min, max);
	if(solution)
	    attributeDesc.setIsSolution(true);
	return attributeDesc;
    }

    public boolean deleteAllAttribute(String conceptID) {
	Concept subConcept = project.getSubConcepts().get(conceptID);
	for(String attributeDescName : subConcept.getAllAttributeDescs().keySet()) {
	    subConcept.removeAttributeDesc(attributeDescName);
	}
	return true;
    }

    public Map<String, Object> getAttributeByID(String conceptID, String attributeID) {
	Concept subConcept = project.getSubConcepts().get(conceptID);
	attributeDesc = subConcept.getAttributeDesc(attributeID);
	
	HashMap<String, AttributeDesc> allAttributeDescs = subConcept.getAllAttributeDescs();

	if(!allAttributeDescs.containsKey(attributeID))
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
    
    /*
     public Map<String,Object> getAttributeDiscription() {

	Map<String, Object> map = attributeDesc.getRepresentation();

	addRangeIfAbsent(map);

	return map;
    } 
    */
    
    private void addRangeIfAbsent(Map<String, Object> map) {
	if ( !map.containsKey(RANGE)) {

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
		logger.error("No matching AttributeDescription found for : "+attributeDescName);
		map.putIfAbsent(RANGE, "n/a");
		break;
	    } 
	}
    }
}
