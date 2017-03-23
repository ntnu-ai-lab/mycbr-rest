package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.casebase.Instance;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
@RestController
public class CBRController {

    @ApiOperation(value = "getCase", nickname = "getCase")
    @RequestMapping(method = RequestMethod.GET, value = "/case", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Case.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Case getCase(@RequestParam(value="caseID", defaultValue="144_vw") String caseID) {
        return new Case(caseID);
    }

    @ApiOperation(value = "getSimilarCases", nickname = "getSimilarCases")
    @RequestMapping(method = RequestMethod.POST, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Query getSimilarCases(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true)  HashMap<String, Object> queryContent) {
        return new Query(casebase, concept, amalFunc, queryContent, k);
    }

    @ApiOperation(value = "getSimilarCasesByID", nickname = "getSimilarCasesByID")
    @RequestMapping(method = RequestMethod.GET, path="/retrievalByID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Query getSimilarCasesByID(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {
        return new Query(casebase, concept, amalFunc, caseID, k);
    }

    @ApiOperation(value = "getSimilarCasesByAttribute", nickname = "getSimilarCases")
    @RequestMapping(method = RequestMethod.GET, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Query getSimilarCasesByAttribute(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {
        return new Query(casebase, concept, amalFunc, attribute, value, k);
    }

    @ApiOperation(value = "getSimilarCasesWithContent", nickname = "getSimilarCasesWithContent")
    @RequestMapping(method = RequestMethod.POST, path="/retrievalWithContent", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true)  HashMap<String, Object> queryContent) {

        Query query = new Query(casebase, concept, amalFunc, queryContent, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        return cases;
    }

    @ApiOperation(value = "getSimilarCasesByIDWithContent", nickname = "getSimilarCasesByIDWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/retrievalByIDWithContent", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesByIDWithContent(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept", defaultValue="Car") String concept,
            @RequestParam(value="amalgamation function", defaultValue="CarFunc") String amalFunc,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {

        Query query = new Query(casebase, concept, amalFunc, caseID, k);
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
        return cases;
    }

    @ApiOperation(value = "getSimilarCasesByAttributeWithContent", nickname = "getSimilarCasesByAttributeWithContent")
    @RequestMapping(method = RequestMethod.GET, path="/retrievalWithContent", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesByAttributeWithContent(
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

    @ApiOperation(value = "getCaseBases", nickname = "getCaseBases")
    @RequestMapping(method = RequestMethod.GET, path="/casebase", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = CaseBases.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public CaseBases getCaseBases() {
        return new CaseBases();
    }

    @ApiOperation(value = "getAmalgamationFunctions", nickname = "getAmalgamationFunctions")
    @RequestMapping(method = RequestMethod.GET, path="/amalgamationFunctions", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AmalgamationFunctions.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public AmalgamationFunctions getAmalgamationFunctions(@RequestParam(value="concept name", defaultValue="Car") String concept) {
        return new AmalgamationFunctions(concept);
    }

    @ApiOperation(value = "getConcept", nickname = "getConcept")
    @RequestMapping(method = RequestMethod.GET, path="/concepts", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConceptName.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ConceptName getConcept() {
        return new ConceptName();
    }

    @ApiOperation(value = "getAttributes", nickname = "getAttributes")
    @RequestMapping(method = RequestMethod.GET, value = "/attributes", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ApiResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Attribute getAttributes(@RequestParam(value="concept name", defaultValue="Car") String concept) {
        return new Attribute(concept);
    }

    @ApiOperation(value = "getValueRange", nickname = "getValueRange")
    @RequestMapping(method = RequestMethod.GET, value = "/values", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ValueRange getValueRange(
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="attribute name", defaultValue="Color") String attributeName) {

        return new ValueRange(concept, attributeName);
    }

    private List<LinkedHashMap<String, String>> getFullResult(Query query, String concept) {
        LinkedHashMap<String, Double> results = query.getSimilarCases();
        List<LinkedHashMap<String, String>> cases = new ArrayList<>();

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            String entryCaseID = entry.getKey();
            double similarity = entry.getValue();
            Case caze = new Case(concept, entryCaseID, similarity);
            cases.add(caze.getCase());
        }

        return cases;
    }
}
