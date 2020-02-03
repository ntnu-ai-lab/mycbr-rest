package no.ntnu.mycbr.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.controller.helper.Query;
import no.ntnu.mycbr.rest.controller.service.AnalyticsService;
import no.ntnu.mycbr.rest.controller.service.SelfSimilarityRetrieval;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import static no.ntnu.mycbr.rest.utils.QueryUtils.getFullResult;

import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;
import static no.ntnu.mycbr.rest.controller.service.AnalyticsService.*;
import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;
import static no.ntnu.mycbr.rest.utils.QueryUtils.getFullResult;


@RestController
public class RetrievalController {

    private static final String RETRIEVAL = "/retrieval";
    private static final String RETRIEVAL_BY_CASE_ID = "/retrievalByCaseID";
    private static final String RETRIEVAL_BY_MULTIPLE_CASE_I_DS = "/retrievalByMultipleCaseIDs";
    private static final String RETRIEVAL_BY_ATTRIBUTE = "/retrievalByAttribute";
    private static final String RETRIEVAL_BY_MULTIPLE_ATTRIBUTES = "/retrievalByMultipleAttributes";
    private static final String RETRIEVAL_BY_CASE_ID_WITH_CONTENT = "/retrievalByCaseIDWithContent";
    private static final String RETRIEVAL_WITH_CONTENT = "/retrievalWithContent";
    
    private static final String VW = "vw";
    private static final String CAR0 = "car0";
    private static final String MANUFACTURER = "manufacturer";
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Redundant code
     * @deprecated use {@link #getSimilarInstancesWithContent(String, String, String, int, HashMap)} instead.  
     */
    @Deprecated
    @ApiOperation(value = "use: "+GET_SIMILAR_CASES_BY_MULTIPLE_ATTRIBUTES, nickname = "use: "+GET_SIMILAR_CASES_BY_MULTIPLE_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.POST, path=PATH_CONCEPT_CASEBASE_ID+RETRIEVAL, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Query getSimilarInstances(
	    @RequestParam(value=CASEBASE, defaultValue=DEFAULT_CASEBASE) String casebase,
	    @RequestParam(value=CONCEPT_ID, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=AMAL_FUNCTION, defaultValue=DEFAULT_AMAL_FUNCTION) String amalFunc,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true) HashMap<String, Object> queryContent) {
	
	return new Query(casebase, concept, amalFunc, queryContent, k);
    }

    @ApiOperation(value = GET_SIMILAR_CASES_BY_CASE_ID, nickname = GET_SIMILAR_CASES_BY_CASE_ID)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID+RETRIEVAL_BY_CASE_ID, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Double> getSimilarCasesByID(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value=CASE_ID) String caseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	Query query = new Query(casebaseID, conceptID, amalgamationFunctionID, caseID, k);
	
	return query.getSimilarCases();
    }

    @ApiOperation(value = GET_SIMILAR_CASES_BY_MULTIPLE_CASE_IDS, nickname = GET_SIMILAR_CASES_BY_MULTIPLE_CASE_IDS)
    @RequestMapping(method = RequestMethod.POST, path=PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID+RETRIEVAL_BY_MULTIPLE_CASE_I_DS, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, HashMap<String, Double>> getSimilarCasesByIDs(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseIDs,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Set<String> caseIDs) {
	
	Map<String, HashMap<String, Double>> retrievedResult  = Query.retrieve(casebaseIDs, conceptID, amalgamationFunctionID, caseIDs, k);
	
	return retrievedResult;
    }

    /**
     * Redundant code
     * @deprecated use {@see no.ntnu.mycbr.rest.controller.EphemeralController#retrievalFromEphemeralCaseBaseWithContent(String, String, String, String, int, Set)} instead. 
     */
    @Deprecated
    @ApiOperation(value = "use: "+ GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE_WITH_CONTENT, nickname = "use: "+ GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE_WITH_CONTENT)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_ID+"/retrievalByIDInIDs", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDWithinIDs(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @RequestParam(required = true)  Set<String> caseIDs,
	    @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	JSONParser parser = new JSONParser();
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

    /**
     * Redundant code
     * @deprecated use {@see no.ntnu.mycbr.rest.controller.EphemeralController#retrievalFromEphemeralCaseBase(String, String, String, int, Map)} instead.  
     */
    @Deprecated
    @ApiOperation(value = "use: "+ GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE, nickname = "use: "+ GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_ID+"/retrievalByIDsInIDs", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDsWithinIDs(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @RequestParam(required = true)  Set<String> caseIDs,
	    @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	ArrayList<String> caseIDList = new ArrayList<>();
	JSONParser parser = new JSONParser();
	JSONArray inpcases = null;
	
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
	return Query.retrieve(casebaseID, conceptID, null, caseIDs, queryBaseIDs, k);
    }

    @ApiOperation(value = GET_SIMILAR_CASES_BY_ATTRIBUTE, nickname = GET_SIMILAR_CASES_BY_ATTRIBUTE)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID+RETRIEVAL_BY_ATTRIBUTE, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Query getSimilarInstancesByAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value="Symbol attribute name", defaultValue=MANUFACTURER) String attribute,
	    @RequestParam(value=VALUE, defaultValue=VW) String value,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	return new Query(casebaseID, conceptID, amalgamationFunctionID, attribute, value, k);
    }

    @ApiOperation(value = GET_SIMILAR_CASES_BY_MULTIPLE_ATTRIBUTES, nickname = GET_SIMILAR_CASES_BY_MULTIPLE_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.POST, path=PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID+RETRIEVAL_BY_MULTIPLE_ATTRIBUTES, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesWithContent(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  HashMap<String, Object> attributeNameValueMap) {

	Query query = new Query(casebaseID, conceptID, amalgamationFunctionID, attributeNameValueMap, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }

    @ApiOperation(value = GET_SIMILAR_CASES_BY_CASE_ID_WITH_CONTENT, nickname = GET_SIMILAR_CASES_BY_CASE_ID_WITH_CONTENT)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID+RETRIEVAL_BY_CASE_ID_WITH_CONTENT, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByIDWithContent(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value=CASE_ID, defaultValue=CAR0) String caseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

	Query query = new Query(casebaseID, conceptID, amalgamationFunctionID, caseID, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }

    @ApiOperation(value = GET_SIMILAR_CASES_WITH_CONTENT, nickname = GET_SIMILAR_CASES_WITH_CONTENT)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_ID+RETRIEVAL_WITH_CONTENT, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByAttributeWithContent(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,
	    @RequestParam(value=AMAL_FUNCTION_ID, defaultValue=DEFAULT_AMAL_FUNCTION) String amalgamationFunctionID,
	    @RequestParam(value="Symbol attribute name", defaultValue=MANUFACTURER) String attribute,
	    @RequestParam(value=VALUE, defaultValue=VW) String value,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

	Query query = new Query(casebaseID, conceptID, amalgamationFunctionID, attribute, value, k);
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }

    /**
     * Retrieval of similarity values for all the cases of the case base, where every case is queried to the case base.
     * @param conceptID: The name of the case base used in the myCBR project
     * @param casebaseID: The name of the concept used in the myCBR project
     * @param amalgamationFunctionID: Amalgamation function or Global Similarity Function that is used in global similarity computation
     * @param k: Number of retrieved cases desired by the user. Default value is -1, which means return all the cases.
     * @return A matrix of similarity values, where the rows and columns are case IDs. The data structure is map of maps. 
     */
    @ApiOperation(value = GET_CASE_BASE_SELF_SIMILARITY, nickname = GET_CASE_BASE_SELF_SIMILARITY)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_CASEBASE_SELF_SIMLARITY, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Map<String, Double>> getCaseBaseSelfSimilarity(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID,	    
	    @RequestParam(required = false, value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES, defaultValue = DEFAULT_NO_OF_CASES) int k) {
	
	Map<String, Map<String,Double>> retrivalResults =  new SelfSimilarityRetrieval()
		.performSelfSimilarityRetrieval(conceptID, casebaseID, amalgamationFunctionID, k);
	
	return retrivalResults;
    }
}
