package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.controller.service.EphemeralRetrieval;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;
import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;

/**
 * This controller class is responsible to receiving all REST requests pertaining to ephemeral (lasting for 
 * a very short time) case bases.
 * @author Amar Jaiswal
 * @since 20 Nov 2019
 */
@RestController
public class EphemeralController {

    private static final String DEFAULT_CASE_ID = "car0";
    private final Log logger = LogFactory.getLog(getClass());

    //@Autowired
    //private EphemeralRetrieval ephemeralService;

    /**
     * Perform retrieval on an ephemeral case base with a single query (caseID).
     * @param conceptID   : Concept name of the myCBR project.
     * @param casebaseID  : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param queryCaseID : The caseID that serves as a query
     * @param k           : Number of desired retrieved cases per query case.
     * @param ephemeralCaseIDs : The ephemeralCaseIDs is a list of caseIDs used for creating an ephemeral case base.
     * @return List<Map<String, String>> : List<E> - list of cases (E) from the ephemeral case base. 
     * The number of cases in the list depends on the value of k. 
     * Map<K,V> - case representation as a map, where K is the respective attributeID and V is the respective value as a String. 
     * This map includes two additional keys: "caseID" and "similarity" apart from the attributeIDs. 
     * 
     */
   @ApiOperation(value = GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE_WITH_CONTENT, nickname = GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE_WITH_CONTENT)
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_RETRIEVAL_WITH_CONTENT, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<Map<String, String>> retrievalFromEphemeralCaseBaseWithContent(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID, 
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value=CASE_ID, defaultValue=DEFAULT_CASE_ID) String queryCaseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Set<String> ephemeralCaseIDs) {

	EphemeralRetrieval ephemeralRetrieval = new EphemeralRetrieval(conceptID, casebaseID, amalgamationFunctionID, k);
	List<Map<String, String>> retrivalResults = ephemeralRetrieval
		.ephemeralRetrivalForSingleQuery(queryCaseID, ephemeralCaseIDs);

	return retrivalResults;
    }
    
    /**
     * Perform retrieval with multiple queries (caseIDs) on an ephemeral case base.
     * @param conceptID  : Concept name of the myCBR project.
     * @param casebaseID : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param k          : Number of desired retrieved cases per query case.
     * @param mapOfcaseIDs : This map has only two entries (i.e. it contains only two keys: "queryCaseIDs" and "ephemeralCaseIDs". 
     * The value of key <code>queryCaseIDs</code> contains a list of caseIDs to be queried. 
     * The value of key <code>ephemeralCaseIDs</code> contains the list of caseIDs used for creating an ephemeral case base.
     * @return Map<String, Map<String, String>> : Outer Map<K1,V1> - K1: a caseID from the queryCaseIDs, V1: inner map (Map<K2,V2>). 
     * This map will contain all the caseIDs as a key from the queryCaseIDs.
     * Inner Map<K2,V2> - K2: a caseID from the ephemeralCaseIDs, V2: similarity score for K1 vs K2, respectively. 
     * The number of cases in the inner map (Map<K2,V2>) depends on the value of k. 
     */
    @ApiOperation(value = GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE, nickname = GET_SIMILAR_CASES_FROM_EPHEMERAL_CASE_BASE)
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_RETRIEVAL, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Map<String, Double>> retrievalFromEphemeralCaseBase(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID, 
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Map<String, Set<String>> mapOfcaseIDs) {

	Set<String> querySet = mapOfcaseIDs.get(QUERY_CASE_IDS);
	Set<String> cbSet = mapOfcaseIDs.get(EPHEMERAL_CASE_IDS);

	EphemeralRetrieval ephemeralRetrieval = new EphemeralRetrieval(conceptID, casebaseID, amalgamationFunctionID, k);
	Map<String, Map<String, Double>> retrivalResults = ephemeralRetrieval.ephemeralRetrival(querySet, cbSet);

	return retrivalResults;
    }
    
    /**
     * Computes Self Similarity on the cases of an ephemeral case base.
     * @param conceptID  : Concept name of the myCBR project.
     * @param casebaseID : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param k          : Number of desired retrieved cases per query case.
     * @param ephemeralCaseIDs : List of caseIDs used for creation ephemeral case base.
     * @return Map<String, Map<String, String>> : Outer Map<K1,V1> - K1: a caseID from the ephemeralCaseIDs, V1: inner map (Map<K2,V2>). 
     * This map will contain all the caseIDs as a key from the ephemeralCaseIDs.
     * Inner Map<K2,V2> - K1: a caseID from the ephemeralCaseIDs, V2: similarity score for K1 vs K2, respectively. 
     * The number of cases in the inner map (Map<K2,V2>) depends on the value of k. 
     */
    @ApiOperation(value = GET_EPHEMERAL_CASE_BASE_SELF_SIMILARITY, nickname = GET_EPHEMERAL_CASE_BASE_SELF_SIMILARITY)
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_SELF_SIMILARITY, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Map<String, Double>> computeEphemeralCaseBaseSelfSimilarity(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=CASEBASE_ID) String casebaseID, 
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Set<String> ephemeralCaseIDs) {

	EphemeralRetrieval ephemeralRetrieval = new EphemeralRetrieval(conceptID, casebaseID, amalgamationFunctionID, k);
	Map<String, Map<String, Double>> retrivalResults = ephemeralRetrieval.computeSelfSimilarity(ephemeralCaseIDs);

	return retrivalResults;
    }
}
