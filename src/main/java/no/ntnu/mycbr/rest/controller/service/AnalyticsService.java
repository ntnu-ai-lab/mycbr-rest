package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Attribute;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.ISimFct;
import no.ntnu.mycbr.core.similarity.Similarity;

import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.utils.ListUtil;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 * Updated on Jan 13, 2020
 */

public class AnalyticsService {

    private Project project;
    private Concept concept;
    //private ICaseBase cb;
    private TemporaryAmalgamFctManager tempAmalgamFctManager;
    
    private List<Map<String, Double>> resultList = new ArrayList<Map<String, Double>>();

    public AnalyticsService(String conceptID, String amalFuncName) {
        this.project = App.getProject();
        this.concept = project.getConceptByID(conceptID);
        //this.cb = (DefaultCaseBase)project.getCaseBases().get(casebaseID);
        
       
         this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	// This will change the default Amalgamation Function of the myCBR project to a user specified function
	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalFuncName);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}
	
    }

    public List<Map<String, Double>> getCaseComparison(String caseID1, String caseID2) {

        Instance case1 = concept.getInstance(caseID1);
        Instance case2 = concept.getInstance(caseID2);

        List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

        resultList.clear();
        for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
            String attrName = sortedAttrDesc.get(i).getName();

            Attribute case1Att = case1.getAttForDesc(sortedAttrDesc.get(i));
            Attribute case2Att = case2.getAttForDesc(sortedAttrDesc.get(i));

            AttributeDesc attrDesc = concept.getAttributeDesc(attrName);
            Number weight = concept.getActiveAmalgamFct().getWeight(attrDesc);

            try {
                ISimFct simfct = (ISimFct) concept.getActiveAmalgamFct().getActiveFct(attrDesc);
                Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
                LinkedHashMap<String, Double> res = new LinkedHashMap<>();
                res.put(attrName, weight.doubleValue() * sim.getValue());
                resultList.add(res);

            } catch (Exception e) {
            }
        }

        return resultList;
    }


    public List<Map<String, Double>> getLocalSimComparison(String caseID1, String caseID2) {
	Instance case1 = concept.getInstance(caseID1);
        Instance case2 = concept.getInstance(caseID2);

        List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

        resultList.clear();
        for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
            String attrName = sortedAttrDesc.get(i).getName();

            Attribute case1Att = case1.getAttForDesc(sortedAttrDesc.get(i));
            Attribute case2Att = case2.getAttForDesc(sortedAttrDesc.get(i));

            AttributeDesc attrDesc = concept.getAttributeDesc(attrName);

            try {
                ISimFct simfct = (ISimFct) concept.getActiveAmalgamFct().getActiveFct(attrDesc);
                Similarity sim = simfct.calculateSimilarity(case1Att, case2Att);
                LinkedHashMap<String, Double> res = new LinkedHashMap<>();
                res.put(attrName, sim.getValue());
                resultList.add(res);

            } catch (Exception e) {
            }
        }

        return resultList;
    }

    public List<Map<String, Double>> getGlobalWeights() {

        List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

        resultList.clear();
        for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
            String attrName = sortedAttrDesc.get(i).getName();
            AttributeDesc desc = concept.getAttributeDesc(attrName);
            Number weight = concept.getActiveAmalgamFct().getWeight(desc);
            LinkedHashMap<String, Double> res = new LinkedHashMap<>();
                res.put(attrName, weight.doubleValue());
                resultList.add(res);
        }
        
        return resultList;
    }
}