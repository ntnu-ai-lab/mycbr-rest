package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.Query;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.service.SelfSimilarityRetrieval;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static no.ntnu.mycbr.rest.utils.QueryUtils.getFullResult;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.Constants.*;

@RestController
public class RetrievalController {

    private final Log logger = LogFactory.getLog(getClass());

    @ApiOperation(value = "getSimilarInstances", nickname = "getSimilarInstances")
    @RequestMapping(method = RequestMethod.POST, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrieval", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public Query getSimilarInstances(
	    @RequestParam(value=CASEBASE_STR, defaultValue=DEFAULT_CASEBASE) String casebase,
	    @RequestParam(value=CONCEPT_ID_STR, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true) HashMap<String, Object> queryContent) {
	return new Query(casebase, concept, amalFunc, queryContent, k);
    }

    @ApiOperation(value = "getSimilarInstancesByID", nickname = "getSimilarInstancesByID")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByID", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public Query getSimilarCasesByID(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value=CASE_ID_STR) String caseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	return new Query(casebaseID, conceptID, null, caseID, k);
    }

    @ApiOperation(value = "getSimilarInstancesByIDs", nickname = "getSimilarInstancesByIDs")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByIDs", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarCasesByIDs(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseIDs,
	    @RequestParam(value=CASE_IDS_STR) String caseIDsJson,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	ArrayList<String> caseIDs = new ArrayList<>();
	JSONParser parser = new JSONParser();
	JSONArray inpcases = null;
	try {
	    inpcases = (JSONArray) parser.parse(caseIDsJson);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	Iterator<String>  it = inpcases.iterator();
	while(it.hasNext())
	    caseIDs.add(it.next());
	return Query.retrieve(casebaseIDs, conceptID, null, caseIDs, k);
    }

    @ApiOperation(value = "getSimilarInstancesByIDWithinIDs", nickname = "getSimilarInstancesByIDWithinIDs")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByIDInIDs", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDWithinIDs(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value=CASE_ID_STR, defaultValue = "queryCaseID1") String caseID,
	    @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	JSONParser parser = new JSONParser();
	ArrayList<String> caseIDs = new ArrayList<>();
	caseIDs.add(caseID);
	ArrayList<String> queryBaseIDs = new ArrayList<>();
	JSONArray queryBase = null;
	try {
	    queryBase = (JSONArray) parser.parse(filterCaseIDs);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	Iterator<String>  it = queryBase.iterator();
	it = queryBase.iterator();
	while(it.hasNext())
	    queryBaseIDs.add(it.next());
	return Query.retrieve(casebaseID, conceptID, null, caseIDs, queryBaseIDs, k);
    }

    @ApiOperation(value = "getSimilarInstancesByIDsWithinIDs", nickname = "getSimilarInstancesByIDWithinIDs")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByIDsInIDs", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDsWithinIDs(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value=CASE_IDS_STR, defaultValue = "[queryCase1, queryCase2]") String caseIDs,
	    @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	ArrayList<String> caseIDList = new ArrayList<>();
	JSONParser parser = new JSONParser();
	JSONArray inpcases = null;
	try {
	    inpcases = (JSONArray) parser.parse(caseIDs);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	Iterator<String>  it = inpcases.iterator();
	while(it.hasNext())
	    caseIDList.add(it.next());
	ArrayList<String> queryBaseIDs = new ArrayList<>();
	JSONArray queryBase = null;
	try {
	    queryBase = (JSONArray) parser.parse(filterCaseIDs);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	it = queryBase.iterator();
	while(it.hasNext())
	    queryBaseIDs.add(it.next());
	return Query.retrieve(casebaseID, conceptID, null, caseIDList, queryBaseIDs, k);
    }

    @ApiOperation(value = "getSimilarInstancesByAttribute", nickname = "getSimilarInstances")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByAttribute", produces=APPLICATION_JSON)
    @ApiResponsesForQuery
    public Query getSimilarInstancesByAttribute(
	    @RequestParam(value=CASEBASE_STR, defaultValue=DEFAULT_CASEBASE) String casebase,
	    @RequestParam(value=CONCEPT_ID_STR, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
	    @RequestParam(value="value", defaultValue="vw") String value,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	return new Query(casebase, concept, amalFunc, attribute, value, k);
    }

    @ApiOperation(value = "getSimilarInstancesWithContent", nickname = "getSimilarInstancesWithContent")
    @RequestMapping(method = RequestMethod.POST, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalWithContent", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesWithContent(
	    @RequestParam(value=CASEBASE_STR, defaultValue=DEFAULT_CASEBASE) String casebase,
	    @RequestParam(value=CONCEPT_ID_STR, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  HashMap<String, Object> queryContent) {

	Query query = new Query(casebase, concept, amalFunc, queryContent, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
	return cases;
    }

    @ApiOperation(value = "getSimilarInstancesByIDWithContent", nickname = "getSimilarInstancesByIDWithContent")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalByIDWithContent", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByIDWithContent(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(value=CASE_ID_STR, defaultValue="144_vw") String caseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

	Query query = new Query(casebaseID, conceptID, amalFunc, caseID, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }

    @ApiOperation(value = "getSimilarInstancesByAttributeWithContent", nickname = "getSimilarInstancesByAttributeWithContent")
    @RequestMapping(method = RequestMethod.GET, path=PATH_DEFAULT_CONCEPT_CASEBASE+"retrievalWithContent", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByAttributeWithContent(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
	    @RequestParam(value="value", defaultValue="vw") String value,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

	Query query = new Query(casebaseID, conceptID, amalFunc, attribute, value, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }

    /**
     * Retrieval of similarity values for all the cases of the case base, where every case is queried to the case base.
     * @param casebase: The name of the case base used in the myCBR project
     * @param concept: The name of the concept used in the myCBR project
     * @param amalFunc: Amalgamation function or Global Similarity Function that is used in global similarity computation
     * @param k: Number of retrieved cases desired by the user. Default value is -1, which means return all the cases.
     * @return A matrix of similarity values, where the rows and columns are case IDs. The data structure is map of maps. 
     */
    @ApiOperation(value = "getCaseBaseSelfSimilarity", nickname = "getCaseBaseSelfSimilarity")
    @RequestMapping(method = RequestMethod.GET, path=PATH_COMPUTE_SELF_SIMLARITY, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Map<String, Double>> getCaseBaseSelfSimilarity(
	    @RequestParam(value=CONCEPT_ID_STR, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=CASEBASE_STR, defaultValue=DEFAULT_CASEBASE) String casebase,	    
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES, defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	SelfSimilarityRetrieval selfSimilarityRetrieval = new SelfSimilarityRetrieval();
	Map<String, Map<String,Double>> retrivalResults = selfSimilarityRetrieval.performSelfSimilarityRetrieval(casebase, concept, amalFunc, k);
	
	return retrivalResults;
    }
}
