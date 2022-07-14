package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.DoubleFct;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.core.similarity.config.NumberConfig;
import no.ntnu.mycbr.rest.App;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
public class ConceptService {
    Project p = App.getProject();
    private final Log logger = LogFactory.getLog(getClass());

    public Concept addConcept(String conceptID){
        logger.info("creating concept with id:"+conceptID);
        int concepts = p.getSubConcepts().size();
        if (concepts == 0 )
            return createTopConcept(conceptID);
        else if (p.getSuperConcept() == null)
            return createTopConcept(conceptID);
        else {
            try {
                Concept c = new Concept(conceptID,p,p.getSuperConcept());
                return c;
            } catch (Exception e) {
                logger.error("got exception trying to create concept:" , e);
            }
        }
        return null;
    }
    public AttributeDesc addDoubleAttribute(Concept c, String attributeName, double min, double max, boolean solution) throws Exception {
        AttributeDesc attributeDesc = new DoubleDesc(c, attributeName, min, max);
        if(solution)
            attributeDesc.setIsSolution(true);
        return attributeDesc;
    }
    public AttributeDesc addStringAttribute(Concept c, String attributeName, boolean solution) throws Exception {
        AttributeDesc attributeDesc = new StringDesc(c, attributeName);
        if(solution)
            attributeDesc.setIsSolution(true);
        return attributeDesc;
    }
    public AttributeDesc addSymbolicAttribute(Concept c, String attributeName, Set<String> allowedValues, boolean solution) throws  Exception {
        SymbolDesc attributeDesc = new SymbolDesc(c, attributeName, allowedValues);
        return  attributeDesc;
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
    public boolean addDoubleSimilarityFunction(Concept c, String attributeName, String similarityFunctionName, Double parameter)
    {
        DoubleDesc attributeDesc = (DoubleDesc) c.getAllAttributeDescs().get(attributeName);
        //DoubleFct doubleFct = new DoubleFct(p,attributeDesc,similarityFunctionName);
        DoubleFct doubleFct = attributeDesc.addDoubleFct(similarityFunctionName,true);
        doubleFct.setSymmetric(true);
        doubleFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
        doubleFct.setFunctionParameterL(parameter);
        return true;
    }

    public boolean addAmalgamationFunction(Concept c, String amalgamationFunctionID, String amalgamationFunctionType){
        AmalgamationConfig config = AmalgamationConfig.valueOf(amalgamationFunctionType);
        AmalgamationFct fct = c.addAmalgamationFct(config,amalgamationFunctionID, false);
        c.setActiveAmalgamFct(fct);
        return true;
    }
}
