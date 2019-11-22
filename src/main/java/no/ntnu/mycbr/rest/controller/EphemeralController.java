package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.service.EphemeralService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EphemeralService ephemeralService;

    @ApiOperation(value = "retrievalOnEphemeralCaseBase", nickname = "retrievalOnEphemeralCaseBase")
    @RequestMapping(method = RequestMethod.POST, path=DEFAULT_PATH+"retrievalWithContent", produces=APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody LinkedHashMap<String, LinkedHashMap<String, Double>> getSimilarInstancesWithContent(
	    @RequestParam(value=CASEBASE_STR, defaultValue=DEFAULT_CASEBASE) String casebase,
	    @RequestParam(value=CONCEPT_NAME_STR, defaultValue=DEFAULT_CONCEPT) String concept,
	    @RequestParam(value=AMALGAMATION_FUNCTION_STR, defaultValue=DEFAULT_AMALGAMATION_FUNCTION) String amalFunc,
	    @RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
	    @RequestBody(required = true)  Map<String, Set<String>> ephemeralMap) {

	Set<String> querySet = ephemeralMap.get("query-set");
	Set<String> cbSet = ephemeralMap.get("case-base");

	return ephemeralService.ephemeralRetrival(querySet, cbSet);
    }
}
