package no.ntnu.mycbr.rest.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.CaseBases;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;

@RestController
public class CaseBaseController {

    private final Log logger = LogFactory.getLog(getClass());

    @ApiOperation(value = "getCaseBases", nickname = "getCaseBases")
    @RequestMapping(method = RequestMethod.GET, path="/casebase", produces = "application/json")
    @ApiResponsesForCaseBases
    public CaseBases getCaseBases() {
	return new CaseBases();
    }

    @ApiOperation(value="createCaseBase", nickname="createCaseBase")
    @RequestMapping(method=RequestMethod.PUT, value = "/casebases/{casebase}")
    @ApiResponsesDefault
    public boolean createCaseBase(@PathVariable(value="casebase") String casebase){
	Project p = App.getProject();
	try {
	    p.createDefaultCB(casebase);
	} catch (Exception e) {
	    logger.error("got an error creating the casebase", e);
	}
	return true;
    }

    @ApiOperation(value="deleteCaseBase", nickname="deleteCaseBase")
    @RequestMapping(method=RequestMethod.DELETE, value = "/casebases/{casebase}")
    @ApiResponsesDefault
    public boolean deleteCaseBase(@PathVariable(value="casebase") String casebase){
	Project p = App.getProject();
	try {
	    p.deleteCaseBase(casebase);
	} catch (Exception e) {
	    logger.error("got an error creating the casebase", e);
	}
	return true;
    }
}
