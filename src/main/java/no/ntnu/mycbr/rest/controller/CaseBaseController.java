package no.ntnu.mycbr.rest.controller;

import de.dfki.mycbr.core.Project;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.CaseBases;
import org.apache.commons.logging.Log;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CaseBaseController {
    private final Log logger = LogFactory.getLog(getClass());

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
    @ApiOperation(value="createCaseBase", nickname="createCaseBase")
    @RequestMapping(method=RequestMethod.PUT, value = "/casebases/{casebase}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean createConcept(@PathVariable(value="casebase") String casebase,
                                 @RequestParam(value="concept") String concept){
        Project p = App.getProject();
        try {
            p.createDefaultCB(casebase);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
