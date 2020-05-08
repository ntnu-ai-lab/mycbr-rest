package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.io.XMLExporter;
import no.ntnu.mycbr.rest.App;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * This service class serves all the requests regarding similarity which can be global or local.
 * @author Amar Jaiswal
 * @since May 07, 2019
 */
@Service
public class ProjectService {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Saves teh current state of the project to a new project file whose name must be provided by the user.
     * @param projectName Name of the new project file
     * @return boolean
     */
    public boolean saveProjectAs(String projectName) {
        boolean flag = false;
        Project project = App.getProject();
        logger.debug("Trying to save the current project file as : "+projectName+project.getExtension());

        if (project.getPath() != null && project.getName() != null) {
            XMLExporter.save(project, project.getPath() + projectName + project.getExtension());
            flag = true;
        }

        return flag;
    }
}