package no.ntnu.mycbr.rest.service;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.App;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class ConceptService {
    Project p = App.getProject();
    private final Log logger = LogFactory.getLog(getClass());

    public boolean addConcept(String conceptID){
        logger.info("creating concept with id:"+conceptID);
        int concepts = p.getSubConcepts().size();
        if (concepts == 0 )
            createTopConcept(conceptID);
        else if (p.getSuperConcept() == null)
            createTopConcept(conceptID);
        else {
            try {
                Concept c = new Concept(conceptID,p,p.getSuperConcept());
            } catch (Exception e) {
                logger.error("got exception trying to create concept:" , e);
            }
        }
        return true;
    }

    public Concept createTopConcept(String concept){
        Project p = App.getProject();
        Concept c = null;
        try {
            c = p.createTopConcept(concept);
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.save();
        return c;
    }
    public Project getProject(){
        return p;
    }
}
