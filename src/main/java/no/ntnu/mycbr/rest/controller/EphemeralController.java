package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.service.EphemeralService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.Constants.*;

/**
 * This controller class is responsible to receiving all REST requests pertaining to ephemeral (lasting for 
 * a very short time) case bases.
 * @author Amar Jaiswal
 * @since 20 Nov 2019
 */
@RestController
public class EphemeralController {

    private final Log logger = LogFactory.getLog(getClass());

    //@Autowired
    //private EphemeralService ephemeralService;

    /**
     * Perform retrieval on an ephemeral case base with a single query (caseID).
     * @param conceptID   : Concept name of the myCBR project.
     * @param casebaseID  : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param queryCaseID : The caseID that serves as a query
     * @param k           : Number of desired retrieved cases per query case.
     * @param ephemeralCaseIDs : The ephemeralCaseIDs is a list of caseIDs used for creating an ephemeral case base.
     * @return
     */
   @ApiOperation(value = "getSimilarInstancesFromEphemeralCaseBaseWithContent", nickname = "getSimilarInstancesFromEphemeralCaseBaseWithContent")
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_RETRIEVAL_WITH_CONTENT, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> retrievalFromEphemeralCaseBase(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID, 
	    @PathVariable(value=AMALGAMATION_FUNCTION_ID_STR) String amalgamationFunctionID,
	    @RequestParam(value=CASE_ID_STR, defaultValue="patient0") String queryCaseID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Set<String> ephemeralCaseIDs) {

	EphemeralService ephemeralService = new EphemeralService(conceptID, casebaseID, amalgamationFunctionID, k);
	List<LinkedHashMap<String, String>> retrivalResults = ephemeralService
		.ephemeralRetrivalForSingleQuery(queryCaseID, ephemeralCaseIDs);

	return retrivalResults;
    }
    
    /**
     * Perform retrieval with multiple queries (caseIDs) on an ephemeral case base.
     * @param conceptID  : Concept name of the myCBR project.
     * @param casebaseID : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param k          : Number of desired retrieved cases per query case.
     * @param mapOfcaseIDs : Keys are <code>query-set</code> and <code>casebase-set</code>. The <code>query-set</code> contains list of caseIDs to be queried. The <code>casebase-set</code> contains the list of caseIDs used for creating an ephemeral case base.
     * @return
     */
    @ApiOperation(value = "getSimilarInstancesFromEphemeralCaseBase", nickname = "getSimilarInstancesFromEphemeralCaseBase")
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_RETRIEVAL, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Map<String, Double>> retrievalFromEphemeralCaseBase(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID, 
	    @PathVariable(value=AMALGAMATION_FUNCTION_ID_STR) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Map<String, Set<String>> mapOfcaseIDs) {

	Set<String> querySet = mapOfcaseIDs.get("query-set");
	Set<String> cbSet = mapOfcaseIDs.get("casebase-set");

	EphemeralService ephemeralService = new EphemeralService(conceptID, casebaseID, amalgamationFunctionID, k);
	Map<String, Map<String, Double>> retrivalResults = ephemeralService.ephemeralRetrival(querySet, cbSet);

	return retrivalResults;
    }

    /**
     * Computes Self Similarity on the cases of an ephemeral case base.
     * @param conceptID  : Concept name of the myCBR project.
     * @param casebaseID : Case base name of the myCBR project.
     * @param amalgamationFunctionID : Amalgamation function (global similarity function) of the myCBR project.
     * @param k          : Number of desired retrieved cases per query case.
     * @param caseIDs    : List of caseIDs used for creation ephemeral case base.
     * @return
     */
    @ApiOperation(value = "getEphemeralCaseBaseSelfSimilarityMatrix", nickname = "getEphemeralCaseBaseSelfSimilarityMatrix")
    @RequestMapping(method = RequestMethod.POST, path=PATH_EPHEMERAL_SELF_SIMILARITY, produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Map<String, Double>> computeEphemeralCaseBaseSelfSimilarity(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID, 
	    @PathVariable(value=AMALGAMATION_FUNCTION_ID_STR) String amalgamationFunctionID,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Set<String> caseIDs) {

	EphemeralService ephemeralService = new EphemeralService(conceptID, casebaseID, amalgamationFunctionID, k);
	Map<String, Map<String, Double>> retrivalResults = ephemeralService.computeSelfSimilarity(caseIDs);

	return retrivalResults;
    }
}
