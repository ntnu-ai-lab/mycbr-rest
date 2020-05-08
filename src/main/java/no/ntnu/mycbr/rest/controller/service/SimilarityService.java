package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.model.sim.GlobalSimilarities;
import no.ntnu.mycbr.rest.controller.model.sim.GlobalSimilarity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.awt.image.ImageProducer;
import java.util.List;
import java.util.Map;

/**
 * This service class serves all the requests regarding similarity which can be global or local.
 * @author Amar Jaiswal
 * @since May 07, 2019
 */
@Service
public class SimilarityService {

    private final Log logger = LogFactory.getLog(getClass());

    public boolean copyPasteGlobalSim(String conceptID, String sourceSimName, String destinationSimName) {

        boolean flag = false;

        AmalgamationFct sourceAmalgamationFunction = null;
        AmalgamationFct destinationAmalgamationFunction = null;

        Concept concept = App.getProject().getConceptByID(conceptID);

        List<AmalgamationFct> amalgamationFunctions = concept.getAvailableAmalgamFcts();

        for (AmalgamationFct amalgamationFct: amalgamationFunctions){
            if (sourceSimName.equalsIgnoreCase(amalgamationFct.getName())){
                sourceAmalgamationFunction = amalgamationFct;
                destinationAmalgamationFunction = concept.addAmalgamationFct(amalgamationFct.getType(), destinationSimName, false);
            }
        }

        flag = copyPasteGlobalSim(sourceAmalgamationFunction, destinationAmalgamationFunction);

        return flag;
    }

    public boolean copyPasteGlobalSim(AmalgamationFct source, AmalgamationFct target) {
        boolean flag = false;

        Concept concept = source.getConcept();

        Map<String, AttributeDesc> attributeDescs =  concept.getAllAttributeDescs();

        for ( String key : attributeDescs.keySet()){
            AttributeDesc sourceAttributeDesc = attributeDescs.get(key);
            AttributeDesc targetAttributeDesc = target.getConcept().getAttributeDesc(sourceAttributeDesc.getName());

            target.setWeight(targetAttributeDesc , source.getWeight(sourceAttributeDesc));
            target.setActive(targetAttributeDesc , source.isActive(sourceAttributeDesc));
            target.setActiveFct(targetAttributeDesc , source.getActiveFct(sourceAttributeDesc));
        }

        flag = true;

        return flag;
    }
}