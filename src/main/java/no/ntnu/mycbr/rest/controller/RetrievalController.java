package no.ntnu.mycbr.rest.controller;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.ntnu.mycbr.rest.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static no.ntnu.mycbr.rest.utils.RESTCBRUtils.getFullResult;

@RestController
public class RetrievalController {
    private final Log logger = LogFactory.getLog(getClass());

    @ApiOperation(value = "getSimilarInstances", nickname = "getSimilarInstances")
    @RequestMapping(method = RequestMethod.POST, path="/concepts/{conceptID}/casebases/{casebaseID}/retrieval", produces = "application/json")
    @ApiResponsesForQuery
    public Query getSimilarInstances(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true) HashMap<String, Object> queryContent) {
        return new Query(casebase, concept, amalFunc, queryContent, k);
    }

    @ApiOperation(value = "getSimilarInstancesByID", nickname = "getSimilarInstancesByID")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByID", produces = "application/json")
    @ApiResponsesForQuery
    public Query getSimilarCasesByID(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="caseID") String caseID,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {
        return new Query(casebaseID, conceptID, null, caseID, k);
    }

    @ApiOperation(value = "getSimilarInstancesByIDs", nickname = "getSimilarInstancesByIDs")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDs", produces = "application/json")
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarCasesByIDs(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseIDs,
            @RequestParam(value="caseIDs") String caseIDsJson,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {
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
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDInIDs", produces = "application/json")
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDWithinIDs(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="caseID", defaultValue = "queryCaseID1") String caseID,
            @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {
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
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDsInIDs", produces = "application/json")
    @ApiResponsesForQuery
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDsWithinIDs(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="caseIDs", defaultValue = "[queryCase1, queryCase2]") String caseIDs,
            @RequestParam(value="filterCaseIDs", defaultValue = "[caseID1, caseID2, caseID3]") String filterCaseIDs,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {
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
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByAttribute", produces = "application/json")
    @ApiResponsesForQuery
    public Query getSimilarInstancesByAttribute(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {
        return new Query(casebase, concept, amalFunc, attribute, value, k);
    }

    @ApiOperation(value = "getSimilarInstancesWithContent", nickname = "getSimilarInstancesWithContent")
    @RequestMapping(method = RequestMethod.POST, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent", produces = "application/json")
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true)  HashMap<String, Object> queryContent) {

        Query query = new Query(casebase, concept, amalFunc, queryContent, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        return cases;
    }

    @ApiOperation(value = "getSimilarInstancesByIDWithContent", nickname = "getSimilarInstancesByIDWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDWithContent", produces = "application/json")
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByIDWithContent(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {

        Query query = new Query(casebaseID, conceptID, amalFunc, caseID, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
        return cases;
    }

    @ApiOperation(value = "getSimilarInstancesByAttributeWithContent", nickname = "getSimilarInstancesByAttributeWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent", produces = "application/json")
    @ApiResponsesDefault
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByAttributeWithContent(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {

        Query query = new Query(casebaseID, conceptID, amalFunc, attribute, value, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
        return cases;
    }

 // All ApiResponses annotations definitions----------------------------------------------------------------------------------------------------------   

 	@ApiResponses(value = {
 			@ApiResponse(code = 200, message = SUCCESS),
 			@ApiResponse(code = 401, message = UNAUTHORIZED),
 			@ApiResponse(code = 403, message = FORBIDDEN),
 			@ApiResponse(code = 404, message = NOT_FOUND),
 			@ApiResponse(code = 500, message = FAILURE)
 	})
 	private @interface ApiResponsesDefault{}

 	@ApiResponses(value = {
 			@ApiResponse(code = 200, message = SUCCESS, response = Query.class),
 			@ApiResponse(code = 401, message = UNAUTHORIZED),
 			@ApiResponse(code = 403, message = FORBIDDEN),
 			@ApiResponse(code = 404, message = NOT_FOUND),
 			@ApiResponse(code = 500, message = FAILURE)
 	})
 	private @interface ApiResponsesForQuery{}
 	
	private static final String SUCCESS = "Success";	
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String FORBIDDEN = "Forbidden";	
	private static final String NOT_FOUND = "Not Found";
	private static final String FAILURE = "Failure";   


}
