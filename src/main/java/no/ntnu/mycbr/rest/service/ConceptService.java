package no.ntnu.mycbr.rest.service;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.App;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public boolean deleteAllConcepts(){
        Project proj = App.getProject();
        HashMap<String,Concept> subConcepts = proj.getSubConcepts();
        Concept superConcept = proj.getSuperConcept();
        if(superConcept == null && subConcepts.size() == 0)
            return true;
        else if(superConcept == null && subConcepts.size() > 0){
            for(String key : subConcepts.keySet()){
                Concept c = subConcepts.remove(key);
                logger.info("removing Concept "+c.getName());
            }
            return true;
        }
        return recDeleteConcept(superConcept);
    }
    //recursively deletes a concept and all subconcepts
    private static boolean recDeleteConcept(Concept superConcept){
        HashMap<String,Concept> subConcepts = superConcept.getAllSubConcepts();
        boolean ret = true;
        for(Iterator<Map.Entry<String,Concept>> it = subConcepts.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Concept> entry = it.next();
            recDeleteConcept(entry.getValue());
            it.remove();
        }
        superConcept.getSuperConcept().removeSubConcept(superConcept.getName());
        return ret;
    }
}
