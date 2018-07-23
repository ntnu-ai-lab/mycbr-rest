package no.ntnu.mycbr.rest.controller;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.ntnu.mycbr.rest.Query;
import no.ntnu.mycbr.rest.utils.CSVTable;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Query getSimilarCasesByID(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="caseID") String caseID,
            @RequestParam(required = false, value="k",defaultValue = "-1") int k) {
        return new Query(casebaseID, conceptID, null, caseID, k);
    }

    @ApiOperation(value = "getSimilarInstancesByID", nickname = "getSimilarInstancesByID")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDs", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
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

    @ApiOperation(value = "getSimilarInstancesByID", nickname = "getSimilarInstancesByIDWithinIDs")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDsInIDs", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public HashMap<String, HashMap<String,Double>> getSimilarInstancesByIDWithinIDs(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseIDs,
            @RequestParam(value="caseIDs") String caseIDsJson,
            @RequestParam(value="querybaseIDset") String querybaseIDsJson,
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
        ArrayList<String> queryBaseIDs = new ArrayList<>();
        JSONArray queryBase = null;
        try {
            queryBase = (JSONArray) parser.parse(querybaseIDsJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        it = queryBase.iterator();
        while(it.hasNext())
            queryBaseIDs.add(it.next());
        return Query.retrieve(casebaseIDs, conceptID, null, caseIDs, queryBaseIDs, k);
    }

    @ApiOperation(value = "getSimilarInstancesByAttribute", nickname = "getSimilarInstances")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByAttribute", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
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
    @RequestMapping(method = RequestMethod.POST, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent.json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
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
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDWithContent.json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByIDWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {

        Query query = new Query(casebase, concept, amalFunc, caseID, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        return cases;
    }

    @ApiOperation(value = "getSimilarInstancesByAttributeWithContent", nickname = "getSimilarInstancesByAttributeWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent.json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarInstancesByAttributeWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {

        Query query = new Query(casebase, concept, amalFunc, attribute, value, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        return cases;
    }

    @ApiOperation(value = "getSimilarInstancesWithContent", nickname = "getSimilarInstancesWithContent")
    @RequestMapping(method = RequestMethod.POST, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent.csv", produces = "text/csv")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody String getSimilarInstancesWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="delimiter", defaultValue=";") String delimiter,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true)  HashMap<String, Object> queryContent) {

        Query query = new Query(casebase, concept, amalFunc, queryContent, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
        String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
        return csvTable;
    }


    @ApiOperation(value = "getSimilarInstancesByIDWithContent", nickname = "getSimilarInstancesByIDWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalByIDWithContent.csv", produces = "text/csv")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public String getSimilarInstancesByIDWithContentAsCSV(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(value="delimiter", defaultValue=";") String delimiter,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {

        Query query = new Query(casebase, concept, amalFunc, caseID, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
        String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
        return csvTable;
    }

    @ApiOperation(value = "getSimilarInstancesByAttributeWithContent", nickname = "getSimilarInstancesByAttributeWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{conceptID}/casebases/{casebaseID}/retrievalWithContent.csv", produces = "text/csv")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody String getSimilarInstancesByAttributeWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(value="delimiter", defaultValue=";") String delimiter,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {

        Query query = new Query(casebase, concept, amalFunc, attribute, value, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
        String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
        return csvTable;
    }

}
