package no.ntnu.mycbr.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.controller.service.ProjectAccessService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static no.ntnu.mycbr.rest.common.ApiOperationConstants.SAVE_PROJECT;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.ACCEPT_APPLICATION_JSON;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.PATH_PROJECT_SAVE;

@RestController
public class ProjectController {

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ProjectAccessService projectAccessService;

    @Operation(summary = SAVE_PROJECT, operationId = SAVE_PROJECT)
    @RequestMapping(method = RequestMethod.GET, value = PATH_PROJECT_SAVE,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean saveProject() {
        try {
            Project p = projectAccessService.getProject();
            //p.save();
            return true;
        } catch (Exception e) {
            logger.error("Error saving project", e);
            return false;
        }
    }

}
