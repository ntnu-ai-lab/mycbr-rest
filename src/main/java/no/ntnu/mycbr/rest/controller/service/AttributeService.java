package no.ntnu.mycbr.rest.controller.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
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
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.DoubleFct;
import no.ntnu.mycbr.core.similarity.ISimFct;
import no.ntnu.mycbr.core.similarity.SimFct;
import no.ntnu.mycbr.core.similarity.config.NumberConfig;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

/**
 * The AttributeService facilitates CRUD operations on attributes of a concept.
 * @author Amar Jaiswal
 * @since 08 Jan 2020
 */
public class AttributeService {

    private final Log logger = LogFactory.getLog(getClass());

    private static final String STRING = "String";
    private static final String RANGE = "range";
    private static final String SIM_FUNC ="simFct";

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

    public Map<String, Object> getActiveAttributes(String amalgamationFunctionID) {
	Map<String, Object> result = new HashMap<String, Object>();

	TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	// This will change the default Amalgamation Function of the myCBR project to a user specified function
	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalgamationFunctionID);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}

	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg, amalgamationFunctionID)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	for (String key : attrDescMap.keySet()) {
	    AttributeDesc attrDesc = attrDescMap.get(key);
	    Number weight = activeAmalg.getWeight(attrDesc);
	    result.put(key, weight.doubleValue());
	}
	return sortByKey(result);
    }

    public Map<String, Object> getAttributeActiveSimilarityFunctions(String amalgamationFunctionID) {
	Map<String, Object> result = new HashMap<String, Object>();

	TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	// This will change the default Amalgamation Function of the myCBR project to a user specified function
	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalgamationFunctionID);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}

	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg, amalgamationFunctionID)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	for (String key : attrDescMap.keySet()) {
	    AttributeDesc attrDesc = attrDescMap.get(key);
	    if ( activeAmalg.isActive(attrDesc)) {
		String simName = ((ISimFct) activeAmalg.getActiveFct(attrDesc)).getName();
		result.put(key, simName);
	    }
	}
	return sortByKey(result);
    }

    public Map<String, Object> getAllAttributeSimilarityFunctionIDs() {
	Map<String, Object> result = new HashMap<String, Object>();

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	for (String key : attrDescMap.keySet()) {
	    result.put(key, getAllSims(attrDescMap, key));
	}
	return sortByKey(result);
    }

    private Set<String> getAllSims(Map<String, AttributeDesc> attrDescMap, String key) {
	AttributeDesc attrDesc = attrDescMap.get(key);
	Map<String, Object> simFunc = (Map<String, Object>) attrDesc.getRepresentation().get(SIM_FUNC);	
	Set<String> simFuncSet = simFunc.keySet();
	return simFuncSet;
    }

    private boolean isActiveAmalgamationFunction(AmalgamationFct activeAmalg, String amalgamationFunctionID) {
	boolean flag = false;

	if (activeAmalg.getName().equalsIgnoreCase(amalgamationFunctionID)) {
	    flag = true;
	} else {
	    logger.error("The active amalgamation is not set to : "+amalgamationFunctionID);
	    logger.warn("The current active amalgamation is : "+activeAmalg.getName());
	}

	return flag;
    }

    private Map<String, Object> sortByKey (Map<String, Object> map) {

	// LinkedHashMap preserves the order of insertion.
	Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();

	ArrayList<String> sortedKeys = new ArrayList<String>(map.keySet()); 

	Collections.sort(sortedKeys);  

	// Display the TreeMap which is naturally sorted 
	for (String key : sortedKeys)  
	    sortedMap.put(key, map.get(key)) ;

	return sortedMap;
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
