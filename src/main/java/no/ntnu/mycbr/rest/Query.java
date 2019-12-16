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
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.rest.utils.ConcurrentCustomer;
import no.ntnu.mycbr.util.Pair;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by kerstin on 05/08/16.
 */
public class Query implements RetrievalCustomer {

    private LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

    public Query(String casebase, String concept) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        List<Instance> cases = (List<Instance>) cb.getCases();

        for (Instance c : cases) {
            if(c.getConcept().getName().contentEquals(concept))
                this.resultList.put(c.getName(), new Double(1.0));
        }
    }
    public Query(String concept) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        HashMap<String, ICaseBase> cbs = project.getCaseBases();
        List<Instance> cases = new ArrayList<>();
        for(ICaseBase iCaseBase : cbs.values()) {
            cases.addAll( iCaseBase.getCases());
        }

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

            Retrieval r = new Retrieval(myConcept, cb,this);
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
                List<Pair<Instance, Similarity>> results = this.results;

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

    public static HashMap<String,HashMap<String,Double>> retrieve(String casebase,
                                                                  String concept,
                                                                  String amalFunc,
                                                                  Set<String> caseIDs,
                                                                  List<String> queryBase,
                                                                  int k) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase ocb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        DefaultCaseBase tcb = null;
        try {
            tcb = project.createDefaultCB("tempCB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collection<Instance> instances = ocb.getCases();
        /*HashMap<String,Instance> instanceMap = .
                collect(Collectors.
                        toMap(Instance::getName, i->i));*/
        HashMap<String,Instance> instanceMap = instances.stream().collect(HashMap::new,
                (m,c) -> m.put(c.getName(),c),
                (m,u) -> {});
        for(String queryBaseCase : queryBase){
            if(instanceMap.containsKey(queryBaseCase)){
                tcb.addCase(instanceMap.get(queryBaseCase));
            }
        }
        HashMap<String,HashMap<String,Double>> ret = retrieve("tempCB",concept,amalFunc,caseIDs,k);
        project.deleteCaseBase("tempCB");
        return ret;

    }
    public static HashMap<String,HashMap<String,Double>> retrieve(String casebase, String concept, String amalFunc, Set<String> caseIDs, int k) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);
        HashMap<String,HashMap<String,Double>> ret = new HashMap<String,HashMap<String,Double>>();
        ConcurrentCustomer concurrentCustomer = new ConcurrentCustomer();
        TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);

        try {
            if(amalFunc!=null)
        	tempAmalgamFctManager.changeAmalgamFct(amalFunc);

            ExecutorService executor = Executors.newFixedThreadPool(48);
            ArrayList<Retrieval> retrievalThreads = new ArrayList<>();
            HashMap<String,Retrieval> retrievals = new HashMap<>();
            //r.setRetrievalEngine(new NeuralRetrieval(project,r));
            
            for(String caseID : caseIDs) {
                Retrieval r = new Retrieval(myConcept, cb,concurrentCustomer,caseID);
                //concurrentCustomer.addRetriever(r,caseID);
                r.setK(k);
                if(k>0){
                    r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
                }
                try {
                    Instance query = r.getQueryInstance();
                    Instance caze = myConcept.getInstance(caseID);

                    for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
                            .entrySet()) {
                        query.addAttribute(e.getKey(), e.getValue());
                    }
                    retrievals.put(caseID,r);
                    retrievalThreads.add(r);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<Future<RetrievalResult>> futureList = executor.invokeAll(retrievalThreads);
            for(Future<RetrievalResult> future : futureList){
                try {
                    RetrievalResult result = future.get();
                    List<Pair<Instance,Similarity>> thisRetList = result.getResult();
                    HashMap<String,Double> thismap = new HashMap<>();
                    for(Pair<Instance,Similarity> p : thisRetList){
                        thismap.put(p.getFirst().getName(),p.getSecond().getValue());
                    }
                    ret.put(result.getRetrevalID(),thismap);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //executor.shutdown();

            //while(!executor.awaitTermination(100L,TimeUnit.SECONDS)){
            //    Thread.sleep(100);
            //}
/*
            ConcurrentHashMap<String,List<Pair<Instance, Similarity>>> results = concurrentCustomer.getResults();
            for(String caseID : retrievals.keySet()){
                //Retrieval r = retrievals.get(caseID);
                HashMap<String,Double> thisMap = new HashMap<>();
                for (Pair<Instance,Similarity> pair: results.get(caseID)) {
                    thisMap.put(pair.getFirst().getName(), pair.getSecond().getValue());
                }
                ret.put(caseID,thisMap);

            }
*/



        } catch (TemporaryAmalgamFctNotChangedException e )
        {

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //tempAmalgamFctManager.rollBack();
        }
        return ret;
    }

    public Query(String casebase, String concept, String amalFunc, String caseID, int k) {
        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);

        // if an amalgamation Function is specified, set this amalgamation function for the retrieval - otherwise use the currently active function
        if(!(amalFunc == null))
            myConcept.setActiveAmalgamFct(project.getFct(amalFunc));

        try {

            Retrieval r = new Retrieval(myConcept, cb,this);
            if (k > 0) {
                r.setK(k);
                r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
            } else {
                r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            }
            //r.setRetrievalEngine(new NeuralRetrieval(project,r));

                Instance query = r.getQueryInstance();

                Instance caze = myConcept.getInstance(caseID);

                for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
                        .entrySet()) {
                    query.addAttribute(e.getKey(), e.getValue());
                }

                r.start();
                List<Pair<Instance, Similarity>> results = this.results;

                for (Pair<Instance, Similarity> result : results) {
                    this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
                }

                query.reset();
            }

            catch (Exception e) {
                e.printStackTrace();
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

            Retrieval r = new Retrieval(myConcept, cb, this);

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
                List<Pair<Instance, Similarity>> results = this.results;

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

    List<Pair<Instance,Similarity>> results;

    @Override
    public void addResults(Retrieval ret, List<Pair<Instance, Similarity>> results) {

            this.results = results;
    }
}