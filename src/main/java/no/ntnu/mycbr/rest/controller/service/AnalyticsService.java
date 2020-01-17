package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Attribute;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.ISimFct;
import no.ntnu.mycbr.core.similarity.Similarity;

import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.utils.ListUtil;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by kerstin on 05/08/16.
 * Updated Amar on Jan 15, 2020
 */

public class AnalyticsService {

    private static final Log logger = LogFactory.getLog(AnalyticsService.class);


    private Project project;
    private Concept concept;
    private ICaseBase casebase;
    private TemporaryAmalgamFctManager tempAmalgamFctManager;

    private String conceptID;
    private String amalgamationFunctionID;

    private List<Map<String, Double>> resultList = new ArrayList<Map<String, Double>>();

    public AnalyticsService(String conceptID, String amalFuncName) {
	this.conceptID = conceptID;
	this.amalgamationFunctionID = amalFuncName;

	this.project = App.getProject();
	this.concept = project.getConceptByID(conceptID);

	this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	// This will change the default Amalgamation Function of the myCBR project to a user specified function
	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalFuncName);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}
    }

    public AnalyticsService(String conceptID, String casebaseID, String amalgamationFunctionID) {
	this(conceptID,amalgamationFunctionID);
	this.casebase = project.getCB(casebaseID);
    }

    public Map<String, Double> compareTwoCases(String caseID1, String caseID2) {
	Map<String, Double> result = new HashMap<String, Double>();

	Instance case1 = concept.getInstance(caseID1);
	Instance case2 = concept.getInstance(caseID2);

	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	for (String key : attrDescMap.keySet()) {
	    AttributeDesc attrDesc = attrDescMap.get(key);

	    if ( activeAmalg.isActive(attrDesc)) {

		Attribute case1Att = case1.getAttForDesc(attrDesc);
		Attribute case2Att = case2.getAttForDesc(attrDesc);

		Number weight = activeAmalg.getWeight(attrDesc);

		ISimFct simfct = (ISimFct) activeAmalg.getActiveFct(attrDesc);

		try {
		    Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		    result.put(key, weight.doubleValue() * sim.getValue());
		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error(e.getLocalizedMessage());
		}
	    }
	}
	return sortByKey(result);
    }

    public Map<String, Double> computeGlobalSimilarityOfTwoCases(String caseID1, String caseID2) {
	Map<String, Double> result = new HashMap<String, Double>();

	Instance case1 = concept.getInstance(caseID1);
	Instance case2 = concept.getInstance(caseID2);

	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	double totalWeight = 0.0;

	for( AttributeDesc desc : attrDescMap.values())
	    totalWeight += activeAmalg.getWeight(desc).doubleValue();

	for (String key : attrDescMap.keySet()) {
	    AttributeDesc attrDesc = attrDescMap.get(key);

	    if ( activeAmalg.isActive(attrDesc)) {

		Attribute case1Att = case1.getAttForDesc(attrDesc);
		Attribute case2Att = case2.getAttForDesc(attrDesc);

		double attrWeight = activeAmalg.getWeight(attrDesc).doubleValue();

		ISimFct simfct = (ISimFct) activeAmalg.getActiveFct(attrDesc);

		try {
		    Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		    double wt = (attrWeight * sim.getValue()) / totalWeight;
		    result.put(key, wt);

		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error(e.getLocalizedMessage());
		}
	    }
	}
	return sortByKey(result);
    }

    public Map<String, Double> computeLocalSimilarityOfTwoCases(String caseID1, String caseID2) {
	Map<String, Double> result = new HashMap<String, Double>();

	Instance case1 = concept.getInstance(caseID1);
	Instance case2 = concept.getInstance(caseID2);

	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	for (String key : attrDescMap.keySet()) {
	    AttributeDesc attrDesc = attrDescMap.get(key);

	    if ( activeAmalg.isActive(attrDesc)) {

		Attribute case1Att = case1.getAttForDesc(attrDesc);
		Attribute case2Att = case2.getAttForDesc(attrDesc);

		ISimFct simfct = (ISimFct) activeAmalg.getActiveFct(attrDesc);

		try {
		    Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		    result.put(key, sim.getValue());
		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error(e.getLocalizedMessage());
		}
	    }
	}
	return sortByKey(result);
    }


    public Map<String, Double> getGlobalWeights() {
	Map<String, Double> result = new HashMap<String, Double>();
	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();

	if (!isActiveAmalgamationFunction(activeAmalg)) {
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


    public Map<String, LinkedHashMap<String, Double>> computeLocalSimilarityWithAllCases(String caseID) {
	Map<String,  LinkedHashMap<String, Double>> result = new  HashMap<String, LinkedHashMap<String, Double>>();

	Instance caze = concept.getInstance(caseID);
	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();
	if (!isActiveAmalgamationFunction(activeAmalg)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();

	List<String> attrNameList = new LinkedList<>();

	for (String key : attrDescMap.keySet()) {
	    if ( activeAmalg.isActive(attrDescMap.get(key))) {
		attrNameList.add(key);
	    }
	}

	// Sort the active attribute names
	Collections.sort(attrNameList);

	// get all the cases from the casebase
	Collection<Instance> cases = casebase.getCases();

	// remove the query case
	cases.remove(caze);

	for (Instance tempCase : cases) {

	    LinkedHashMap<String, Double> caseResult = new LinkedHashMap<>();

	    for (String key : attrNameList) {
		AttributeDesc attrDesc = attrDescMap.get(key);

		Attribute case1Att = caze.getAttForDesc(attrDesc);
		Attribute case2Att = tempCase.getAttForDesc(attrDesc);

		ISimFct simfct = (ISimFct) activeAmalg.getActiveFct(attrDesc);

		try {
		    Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		    caseResult.put(key, sim.getValue());
		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error(e.getLocalizedMessage());
		}
	    }
	    result.put(tempCase.getName(), caseResult);
	}
	return result;
    }
    
    
    public Map<String, LinkedHashMap<String, Double>> computeGlobalSimilarityWithAllCases(String caseID) {
	Map<String,  LinkedHashMap<String, Double>> result = new  HashMap<String, LinkedHashMap<String, Double>>();

	Instance caze = concept.getInstance(caseID);
	AmalgamationFct activeAmalg = concept.getActiveAmalgamFct();
	if (!isActiveAmalgamationFunction(activeAmalg)) {
	    return result;
	}

	Map<String, AttributeDesc> attrDescMap = concept.getAllAttributeDescs();
	
	double totalWeight = 0.0;

	for( AttributeDesc desc : attrDescMap.values())
	    totalWeight += activeAmalg.getWeight(desc).doubleValue();

	List<String> attrNameList = new LinkedList<>();

	for (String key : attrDescMap.keySet()) {
	    if ( activeAmalg.isActive(attrDescMap.get(key))) {
		attrNameList.add(key);
	    }
	}

	// Sort the active attribute names
	Collections.sort(attrNameList);

	// get all the cases from the casebase
	Collection<Instance> cases = casebase.getCases();

	// remove the query case
	cases.remove(caze);

	for (Instance tempCase : cases) {

	    LinkedHashMap<String, Double> caseResult = new LinkedHashMap<>();
	    
	    double globalSim = 0;
	    
	    for (String key : attrNameList) {
		AttributeDesc attrDesc = attrDescMap.get(key);

		Attribute case1Att = caze.getAttForDesc(attrDesc);
		Attribute case2Att = tempCase.getAttForDesc(attrDesc);

		ISimFct simfct = (ISimFct) activeAmalg.getActiveFct(attrDesc);

		double attrWeight = activeAmalg.getWeight(attrDesc).doubleValue();
		
		try {
		    Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		    double wt = (attrWeight * sim.getValue()) / totalWeight;
		    caseResult.put(key, wt);
		    globalSim += wt;
		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error(e.getLocalizedMessage());
		}
	    }
	    caseResult.put("similarity", globalSim);
	    
	    result.put(tempCase.getName(), caseResult);
	}
	return result;
    }



    public List<Map<String, Double>> getCaseComparison(String caseID1, String caseID2) {

	Instance case1 = concept.getInstance(caseID1);
	Instance case2 = concept.getInstance(caseID2);

	// get all the attribute descriptions in alphabetical order by their names.
	List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

	AmalgamationFct amalFact = concept.getActiveAmalgamFct();
	amalFact.getActiveFct(sortedAttrDesc.get(17));
	amalFact.isActive(sortedAttrDesc.get(17));

	resultList.clear();
	for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {

	    String attrName = sortedAttrDesc.get(i).getName();
	    Attribute case1Att = case1.getAttForDesc(sortedAttrDesc.get(i));
	    Attribute case2Att = case2.getAttForDesc(sortedAttrDesc.get(i));

	    AttributeDesc attrDesc = concept.getAttributeDesc(attrName);
	    Number weight = concept.getActiveAmalgamFct().getWeight(attrDesc);

	    try {
		ISimFct simfct = (ISimFct) concept.getActiveAmalgamFct().getActiveFct(attrDesc);
		Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		LinkedHashMap<String, Double> res = new LinkedHashMap<>();
		res.put(attrName, weight.doubleValue() * sim.getValue());
		resultList.add(res);

	    } catch (Exception e) {
	    }
	}

	return resultList;
    }


    public List<Map<String, Double>> getLocalSimComparison(String caseID1, String caseID2) {
	Instance case1 = concept.getInstance(caseID1);
	Instance case2 = concept.getInstance(caseID2);

	List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

	resultList.clear();
	for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
	    String attrName = sortedAttrDesc.get(i).getName();

	    Attribute case1Att = case1.getAttForDesc(sortedAttrDesc.get(i));
	    Attribute case2Att = case2.getAttForDesc(sortedAttrDesc.get(i));

	    AttributeDesc attrDesc = concept.getAttributeDesc(attrName);

	    try {
		ISimFct simfct = (ISimFct) concept.getActiveAmalgamFct().getActiveFct(attrDesc);
		Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
		LinkedHashMap<String, Double> res = new LinkedHashMap<>();
		res.put(attrName, sim.getValue());
		resultList.add(res);

	    } catch (Exception e) {
	    }
	}

	return resultList;
    }





    //****************************** All Private Methods of this Class *************************

    private boolean isActiveAmalgamationFunction(AmalgamationFct activeAmalg) {
	boolean flag = false;

	if (activeAmalg.getName().equalsIgnoreCase(amalgamationFunctionID)) {
	    flag = true;
	} else {
	    logger.error("The active amalgamation is not set to : "+amalgamationFunctionID);
	    logger.warn("The current active amalgamation is : "+activeAmalg.getName());
	}

	return flag;
    }

    private Map<String, Double> sortByKey (Map<String, Double> map) {

	// LinkedHashMap preserves the order of insertion.
	Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();

	ArrayList<String> sortedKeys = new ArrayList<String>(map.keySet()); 

	Collections.sort(sortedKeys);  

	// Display the TreeMap which is naturally sorted 
	for (String key : sortedKeys)  
	    sortedMap.put(key, map.get(key)) ;

	return sortedMap;
    }
}