package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.casebase.Instance;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
    @RequestMapping(method = RequestMethod.GET, path="/retrieval", produces = "application/json")
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
            @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
            @RequestParam(value="value", defaultValue="vw") String value,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {
        return new Query(casebase, concept, attribute, value, k);
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
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k,
            @RequestBody(required = true)  HashMap<String, Object> queryContent) {
        return new Query(casebase, concept, queryContent, k);
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
    public Query getSimilarCases(
            @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept", defaultValue="Car") String concept,
            @RequestParam(value="caseID", defaultValue="144_vw") String caseID,
            @RequestParam(required = false, value="no of returned cases",defaultValue = "-1") int k) {
        return new Query(casebase, concept, caseID, k);
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

}
