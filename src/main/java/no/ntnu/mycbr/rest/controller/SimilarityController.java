package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations;
import no.ntnu.mycbr.rest.controller.model.sim.GlobalSimilarities;
import no.ntnu.mycbr.rest.controller.service.SimilarityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;

/**
 * This controller class is responsible to receiving all REST requests pertaining to similarity functions for
 * a given concept, including attribute level similarity functions.
 * This class only delegates the calls to respective service classes to handle it.
 * @author Amar Jaiswal
 * @since 07 May 2020
 */
@RestController
public class SimilarityController {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimilarityService  simService;

    /**
     * Copy and Paste an existing global similarity function.
     * @param sourceSimName: existing global similarity function name
     * @param destinationSimName: new global similarity function name
     * @return boolean
     */
    @ApiOperation(value = COPY_PASTE_GLOBAL_SIM, nickname = COPY_PASTE_GLOBAL_SIM)
    //@RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS + "/{concept}/globalSims", produces = APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_GLOBAL_SIM_ID, produces = APPLICATION_JSON)
    @ApiResponseAnnotations.ApiResponsesDefault
    public boolean copyPasteGlobalSim(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @RequestParam(value="sourceSimName", required = true, defaultValue = "") String sourceSimName,
            @RequestParam(value="destinationSimName", required = true, defaultValue = "copiedSimFunction") String destinationSimName
        ) {

        return simService.copyPasteGlobalSim(conceptID, sourceSimName, destinationSimName);
    }
}
