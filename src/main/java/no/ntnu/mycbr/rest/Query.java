package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.casebase.MultipleAttribute;
import de.dfki.mycbr.core.casebase.SymbolAttribute;
import de.dfki.mycbr.core.model.*;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;
import no.ntnu.mycbr.CBREngine;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
public class Query {

    private static LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

    public Query(String casebase, String concept, String attribute, String value) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

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

            query.reset();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
}

    public Query(String casebase, String concept, String caseID) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
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

            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

            query.reset();

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Query(String casebase, String concept, HashMap<String, Object> queryContent, int k) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        Retrieval r = new Retrieval(myConcept, cb);

        try {
            Instance query = r.getQueryInstance();

            for (Map.Entry<String, Object> att : queryContent.entrySet()) {
                String name = att.getKey();
                AttributeDesc attdesc = myConcept.getAttributeDesc(name);
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("FloatDesc")){
                    FloatDesc aFloatAtt = (FloatDesc) attdesc;
                    query.addAttribute(attdesc, Float.parseFloat(att.getValue().toString()));
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("IntegerDesc")){
                    IntegerDesc aIntegerAtt = (IntegerDesc) attdesc;
                    query.addAttribute(attdesc, Integer.parseInt(att.getValue().toString()));
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("DoubleDesc")){
                    DoubleDesc aIntegerAtt = (DoubleDesc) attdesc;
                    query.addAttribute(attdesc, Double.parseDouble(att.getValue().toString()));
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("SymbolDesc")){
                    SymbolDesc aSymbolAtt = (SymbolDesc) attdesc;
                    if (!aSymbolAtt.isMultiple()) {
                        query.addAttribute(attdesc, (String) att.getValue());
                    }
                    else {
                        LinkedList<Attribute> llAtts = new LinkedList<Attribute>();
                        StringTokenizer st = new StringTokenizer((String) att.getValue(), ",");
                        while (st.hasMoreElements()) {
                            String symbolName = st.nextElement().toString().trim();
                            llAtts.add(aSymbolAtt.getAttribute(symbolName));
                        }

                        MultipleAttribute<SymbolDesc> muliSymbol = new MultipleAttribute<SymbolDesc>(aSymbolAtt, llAtts);
                        query.addAttribute(attdesc, muliSymbol);
                    }
                }
            }

            //int k = Integer.parseInt(number);
            System.out.println("k: " + k + " and this is > -1: " + (k > -1));
            if (k > -1) {
                r.setK(k);
                r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
                System.out.println("retrieving " + r.getK() + " cases using " + r.getRetrievalMethod());
            } else {
                r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            }

            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();
            System.out.println("retrieved " +  results.size() + " cases using " + r.getRetrievalMethod());

            this.resultList.clear();
            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

            query.reset();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> getSimilarCases() {
        return resultList;
    }
}