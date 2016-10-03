package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.casebase.Instance;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kerstin on 05/08/16.
 */
@RestController
public class CBRController {

    @ApiOperation(value = "getConcept", nickname = "getConcept")
    @RequestMapping(method = RequestMethod.GET, path="/concept", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ConceptName concept() {
        return new ConceptName();
    }



/*    @ApiOperation(value = "getSimilarCases", nickname = "getSimilarCases")
    @RequestMapping(method = RequestMethod.GET, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Query getCases(@RequestParam(value="concept name", defaultValue="Car") String concept,
                          @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
                          @RequestParam(value="value", defaultValue="vw") String value) {
        return new Query(concept, attribute, value);
    }*/

    @ApiOperation(value = "getSimilarCasesByID", nickname = "getSimilarCasesByID")
    @RequestMapping(method = RequestMethod.GET, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Query getSimilarCasesByID(@RequestParam(value="concept", defaultValue="Car") String concept,
                                     @RequestParam(value="caseID", defaultValue="144_vw") String caseID) {
        return new Query(concept, caseID);
    }
    @ApiOperation(value = "getCase", nickname = "getCase")
    @RequestMapping(method = RequestMethod.GET, value = "/case", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Instance.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Case getCase(@RequestParam(value="caseID", defaultValue="144_vw") String caseID) {
        return new Case(caseID);
    }
}
