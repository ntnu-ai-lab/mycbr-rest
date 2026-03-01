package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseBaseService {
    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ProjectAccessService projectAccessService;

    public ICaseBase addCaseBase(String caseBaseID){
        Project p = projectAccessService.getProject();
        ICaseBase ret = null;
        try {
            ret = p.createDefaultCB(caseBaseID);
        } catch (Exception e) {
            logger.error("got exception while trying to create a new case base",e);
        }
        return ret;
    }

    public boolean deleteCaseBase(String caseBaseID){
        Project p = projectAccessService.getProject();
        try {
            p.createDefaultCB(caseBaseID);
        } catch (Exception e) {
            logger.error("got exception while trying to delete a case base",e);
            return false;
        }
        return true;
    }
}
