package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.casebase.MultipleAttribute;
import de.dfki.mycbr.core.model.*;
import de.dfki.mycbr.core.retrieval.NeuralRetrieval;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
public class Query {

    private LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

    public Query(String casebase) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        List<Instance> cases = (List<Instance>) cb.getCases();

        for (Instance c : cases) {
            this.resultList.put(c.getName(), new Double(1.0));
        }
    }

    public Query(String casebase, String concept, String amalFunc, HashMap<String, Object> queryContent, int k) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);

        TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);

        try {
            tempAmalgamFctManager.changeAmalgamFct(amalFunc);

            Retrieval r = new Retrieval(myConcept, cb);
            r.setRetrievalEngine(new NeuralRetrieval(project,r));

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

                if (k > -1) {
                    r.setK(k);
                    r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
                } else {
                    r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
                }

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

        } catch (TemporaryAmalgamFctNotChangedException e) {
            // Return empty result
            return;
        } finally {
            tempAmalgamFctManager.rollBack();
        }
    }

    public Query(String casebase, String concept, String amalFunc, String caseID, int k) {
        System.out.println("in query by id");
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);

        //TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);

        try {
            //tempAmalgamFctManager.changeAmalgamFct(amalFunc);

            Retrieval r = new Retrieval(myConcept, cb);
            r.setRetrievalEngine(new NeuralRetrieval(project,r));
            try {
                Instance query = r.getQueryInstance();

                Instance caze = myConcept.getInstance(caseID);

                for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
                        .entrySet()) {
                    query.addAttribute(e.getKey(), e.getValue());
                }

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

        } finally {
            //tempAmalgamFctManager.rollBack();
        }
    }

    public Query(String casebase, String concept, String amalFunc, String attribute, String value, int k) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);

        try {
            tempAmalgamFctManager.changeAmalgamFct(amalFunc);

            Retrieval r = new Retrieval(myConcept, cb);

            try {
                Instance query = r.getQueryInstance();

                SymbolDesc queryDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get(attribute);
                query.addAttribute(queryDesc, queryDesc.getAttribute(value));

                if (k > -1) {
                    r.setK(k);
                    r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
                } else {
                    r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
                }

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

        } catch (TemporaryAmalgamFctNotChangedException e) {
            // Return empty result
            return;
        } finally {
            tempAmalgamFctManager.rollBack();
        }
    }

    public LinkedHashMap<String, Double> getSimilarCases() {
        return resultList;
    }
}