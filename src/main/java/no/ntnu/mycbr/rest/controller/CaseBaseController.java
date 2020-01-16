package no.ntnu.mycbr.rest.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.rest.App;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;
import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;

@RestController
public class CaseBaseController {

    private final Log logger = LogFactory.getLog(getClass());

    @ApiOperation(value = GET_CASEBASE_IDS, nickname = GET_CASEBASE_IDS)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CASEBASES, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public List<String> getCaseBases() {
	
	List<String> casebaseNames = new LinkedList<>();
	
	Project project = App.getProject();

        for (Map.Entry<String, ICaseBase> cb : project.getCaseBases().entrySet()) {
            casebaseNames.add(cb.getKey());
        }
        
	return casebaseNames;
    }

    @ApiOperation(value=ADD_CASEBASE_ID, nickname=ADD_CASEBASE_ID)
    @RequestMapping(method=RequestMethod.PUT, path=PATH_CASEBASE_ID)
    @ApiResponsesDefault
    public boolean createCaseBase(
	    @PathVariable(value=CASEBASE_ID) String casebase){
	
	Project p = App.getProject();
	try {
	    p.createDefaultCB(casebase);
	} catch (Exception e) {
	    logger.error("got an error creating the casebase", e);
	    return false;
	}
	return true;
    }

    @ApiOperation(value=DELETE_CASEBASE_ID, nickname=DELETE_CASEBASE_ID)
    @RequestMapping(method=RequestMethod.DELETE, path= PATH_CASEBASE_ID)
    @ApiResponsesDefault
    public boolean deleteCaseBase(
	    @PathVariable(value=CASEBASE_ID) String casebase){
	
	Project p = App.getProject();
	try {
	    p.deleteCaseBase(casebase);
	} catch (Exception e) {
	    logger.error("got an error creating the casebase", e);
	}
	return true;
    }
}
