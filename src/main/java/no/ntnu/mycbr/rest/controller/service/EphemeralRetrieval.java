package no.ntnu.mycbr.rest.controller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ntnu.mycbr.core.DefaultCaseBase;
import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Attribute;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.retrieval.Retrieval;
import no.ntnu.mycbr.core.retrieval.Retrieval.RetrievalCustomer;
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.util.Pair;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.helper.Case;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

/**
 * The EphemeralRetrieval facilitates retrieval on an ephemeral case base including Self-Similarity retrieval.
 * @author Amar Jaiswal
 * @since Nov 24, 2019
 */

public class EphemeralRetrieval implements RetrievalCustomer{
    
    // The name used for ephemeral case base
    private static final String EPHEMERAL_CASEBASE_NAME = "ephemeral_casebase";

    // Number of retrieved cases, default is -1 (interpreted as all cases a case base) 
    private int k = -1;
    private Project project;
    private ICaseBase cb;
    private Concept concept;
    private TemporaryAmalgamFctManager tempAmalgamFctManager;

    private List<Pair<Instance,Similarity>> results;
    private Map<String, Map<String, Double>> simMatrix = new LinkedHashMap<String, Map<String, Double>>();

    public EphemeralRetrieval(String conceptName, String casebaseName, String amalFunc, int k) {		
	this.k = k;
	this.project = App.getProject();
	this.cb = (DefaultCaseBase)project.getCaseBases().get(casebaseName);
	this.concept = project.getConceptByID(conceptName);
	
	this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	// This will change the default Amalgamation Function of the myCBR project to a user specified function
	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalFunc);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Query with single caseID on an ephemeral (short lived) case base. The ephemeral case base is a subset of cases from the 
     * default case base.
     * @param query_id : caseID, that will be used as query case against the ephemeral case base.
     * @param cbSet : Set of caseIDs, that will serve as cases of the ephemeral case base.
     * @return List of Maps where key is the caseID of a case, and value is  a map of retrieved similar cases.
     */
    public List<Map<String, String>> ephemeralRetrivalForSingleQuery( String query_id, Set<String> cbSet) {

	ICaseBase motherCasebase = cb;
	ICaseBase ephemeralCasebase = createEmptyCasebase(EPHEMERAL_CASEBASE_NAME, cbSet.size());

	transferCases( cbSet, motherCasebase, ephemeralCasebase);

	Map<String,Double> results = retrieve(concept, ephemeralCasebase, query_id);
	
	List<Map<String, String>> cases = new ArrayList<Map<String, String>>();

        for (Map.Entry<String, Double> entry : results.entrySet()) {
            String entryCaseID = entry.getKey();
            double similarity = entry.getValue();
            Case caze = new Case(concept.getName(), entryCaseID, similarity);
            cases.add(caze.getCase());
        }

        return cases;
    } 
    
   /* public List<Pair<Instance, Similarity>> query(ICaseBase casebase, String query_id) {

        try {

            Retrieval r = new Retrieval(this.concept, ephemeralCasebase, this);
            r.setK(k);
            if(k>0){
                r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
            }
            //r.setRetrievalEngine(new NeuralRetrieval(project,r));

                Instance query = r.getQueryInstance();

                Instance caze = this.concept.getInstance(query_id);

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
        
        return resultList;
    }*/
    
    /**
     * Query with multiple caseIDs son an ephemeral (short lived) case base. The ephemeral case base is a subset of cases from the 
     * default case base.
     * @param querySet Set of caseIDs, that will be used as query cases against the ephemeral case base.
     * @param cbSet Set of caseIDs, that will serve as cases of the ephemeral case base.
     * @return Map of Maps where key is the caseID of a case, and value is  a map of retrieved similar cases.
     */
    public Map<String, Map<String, Double>> ephemeralRetrival( Set<String> querySet, Set<String> cbSet) {

	ICaseBase motherCasebase = cb;
	ICaseBase ephemeralCasebase = createEmptyCasebase(EPHEMERAL_CASEBASE_NAME, cbSet.size());

	transferCases( cbSet, motherCasebase, ephemeralCasebase);

	for(String key: querySet) {
	    // query returns LinkedHashMaps
	    simMatrix.putIfAbsent(key, retrieve(concept, ephemeralCasebase, key));
	}

	return simMatrix;
    }

    /**
     * Get the Self-Similarity matrix for an ephemeral case base.
     * @param cbSet Set of caseIDs, that will serve as cases of the ephemeral case base.
     * @return Map of Maps where key is the caseID of a case, and value is  a map of retrieved similar cases.
     */
    public Map<String, Map<String, Double>> computeSelfSimilarity(Set<String> cbSet){

	ICaseBase motherCasebase = cb;
	ICaseBase ephemeralCasebase = createEmptyCasebase(EPHEMERAL_CASEBASE_NAME, cbSet.size());

	transferCases( cbSet, motherCasebase, ephemeralCasebase);

	Collection<Instance> instances = getAllInstances(ephemeralCasebase);

	for(Instance instance: instances) {
	    String key = instance.getName();
	    // query returns LinkedHashMaps
	    simMatrix.putIfAbsent(key, retrieve(concept, ephemeralCasebase, key));
	}

	return simMatrix;
    }

    private Collection<Instance> getAllInstances(ICaseBase casebase) {
	return casebase.getCases(); 
    }

    private void transferCases(Set<String> caseIds, ICaseBase motherCasebase, ICaseBase ephemeralCasebase) {

	Collection<Instance> instances = motherCasebase.getCases();

	for(Instance instance : instances) {
	    if(caseIds.contains(instance.getName())) {
		ephemeralCasebase.addCase(instance);
	    }
	}
    }

    private ICaseBase createEmptyCasebase(String cbName, int caseCount) {
	ICaseBase cb = null;
	try {
	    cb = new DefaultCaseBase(this.project, cbName, caseCount);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return cb;
    }

    private Map<String, Double> retrieve( Concept concept, ICaseBase casebase, String caseID) {

	Map<String, Double> resultList = new LinkedHashMap<>();

	Retrieval r = new Retrieval(concept, casebase, this);

	Instance query = r.getQueryInstance();

	Instance caze = concept.getInstance(caseID);

	for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
		.entrySet()) {
	    query.addAttribute(e.getKey(), e.getValue());
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
	    resultList.put(result.getFirst().getName(), result.getSecond().getValue());
	}

	query.reset();

	return resultList;
    }

    @Override
    public void addResults(Retrieval ret, List<Pair<Instance, Similarity>> results) {
	this.results = results;
    }
}