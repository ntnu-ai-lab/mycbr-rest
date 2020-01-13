package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.DefaultCaseBase;
import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Attribute;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.casebase.MultipleAttribute;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.retrieval.NeuralRetrieval;
import no.ntnu.mycbr.core.retrieval.Retrieval;
import no.ntnu.mycbr.core.retrieval.Retrieval.RetrievalCustomer;
import no.ntnu.mycbr.core.retrieval.RetrievalResult;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.ISimFct;
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.rest.utils.ConcurrentCustomer;
import no.ntnu.mycbr.rest.utils.ListUtil;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;
import no.ntnu.mycbr.util.Pair;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kerstin on 05/08/16.
 */
public class RetrievalAnalytics {

    private static List<LinkedHashMap<String, Double>> resultList = new ArrayList<>();
    private Concept concept;

    public RetrievalAnalytics(String casebase, String concept, String amalFuncName) {
        Project project = App.getProject();

    }

    public static List<LinkedHashMap<String, Double>> getCaseComparison(String conceptName, String caseAID, String caseBID) {
        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptName) ;
        Instance cazeA = concept.getInstance(caseAID);
        Instance cazeB = concept.getInstance(caseBID);

        List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

        resultList.clear();
        for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
            String attrName = sortedAttrDesc.get(i).getName();

            Attribute caseA_Att = cazeA.getAttForDesc(sortedAttrDesc.get(i));
            Attribute caseB_Att = cazeB.getAttForDesc(sortedAttrDesc.get(i));

            AttributeDesc desc = concept.getAttributeDesc(attrName);
            Number weight = concept.getActiveAmalgamFct().getWeight(desc);

            AmalgamationFct activeAmalFct = concept.getActiveAmalgamFct();
            try {
                ISimFct simfct = (ISimFct) concept.getAvailableAmalgamFcts().get(0).getActiveFct(desc);
                Similarity sim = simfct.calculateSimilarity(caseA_Att, caseB_Att);
                LinkedHashMap<String, Double> res = new LinkedHashMap<>();
                res.put(attrName, weight.doubleValue() * sim.getValue());
                resultList.add(res);

            } catch (Exception e) {
            }
        }

        return resultList;
    }


    public static List<LinkedHashMap<String, Double>> getLocalSimComparison(String conceptName, String caseAID, String caseBID) {
        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptName) ;
        Instance cazeA = concept.getInstance(caseAID);
        Instance cazeB = concept.getInstance(caseBID);

        List<AttributeDesc> sortedAttrDesc = ListUtil.sortAttributeDescs(concept.getAllAttributeDescs().values());

        resultList.clear();
        for (int i = 0; i < concept.getAllAttributeDescs().values().size(); i++) {
            String attrName = sortedAttrDesc.get(i).getName();

            Attribute caseA_Att = cazeA.getAttForDesc(sortedAttrDesc.get(i));
            Attribute caseB_Att = cazeB.getAttForDesc(sortedAttrDesc.get(i));

            AttributeDesc desc = concept.getAttributeDesc(attrName);

            AmalgamationFct activeAmalFct = concept.getActiveAmalgamFct();
            try {
                ISimFct simfct = (ISimFct) concept.getAvailableAmalgamFcts().get(0).getActiveFct(desc);
                Similarity sim = simfct.calculateSimilarity(caseA_Att, caseB_Att);
                LinkedHashMap<String, Double> res = new LinkedHashMap<>();
                res.put(attrName, sim.getValue());
                resultList.add(res);

            } catch (Exception e) {
            }
        }

        return resultList;
    }

    public static List<LinkedHashMap<String, Double>> getGlobalWeights(String conceptName) {
        Project project = App.getProject();
        Concept concept = project.getConceptByID(conceptName) ;

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