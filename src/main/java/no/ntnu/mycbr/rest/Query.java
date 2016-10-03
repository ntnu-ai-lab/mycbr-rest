package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;
import no.ntnu.mycbr.CBREngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kerstin on 05/08/16.
 */
public class Query {

    private static HashMap<String, Double> resultList = new HashMap<String, Double>();;

    public Query(String concept, String attribute, String value) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(CBREngine.getCaseBase());
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        String cases = "none";
        Retrieval r = new Retrieval(myConcept, cb);


        try {
            Instance query = r.getQueryInstance();

            SymbolDesc queryDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get(attribute);
            query.addAttribute(queryDesc,queryDesc.getAttribute(value));

            r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();

            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
}

    public Query(String concept, String caseID) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(CBREngine.getCaseBase());
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        Retrieval r = new Retrieval(myConcept, cb);

        try {
            Instance query = r.getQueryInstance();

            Instance caze = myConcept.getInstance(caseID);

            for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
                    .entrySet()) {
                query.addAttribute(e.getKey(), e.getValue());
            }

            r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();
            System.out.println("Query results: " +results.size());

            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> getSimilarCases() {
        return resultList;
    }

}