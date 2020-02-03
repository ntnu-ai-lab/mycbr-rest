package no.ntnu.mycbr.rest.controller.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class SelfSimilarityRetrieval implements RetrievalCustomer{

    private final Log logger = LogFactory.getLog(getClass());

    private int k = -1;
    
    private List<Pair<Instance,Similarity>> results;
    private Map<String, Map<String, Double>> selfSimMatrix = new LinkedHashMap<String, Map<String, Double>>();

     /**
     * Get the Self-Similarity matrix for a case base.
     * @param casebase Name of the case base.
     * @param conceptName Name of the concept.
     * @param amalFunc Name of the amalgamation function (global similarity function).
     * @param k Number of retrieved cases per retrieval.
     * @return Map of Maps where key is the caseID of a case, and value is  a map of retrieved similar cases.
     */
    public Map<String, Map<String, Double>> performSelfSimilarityRetrieval( String conceptName, 
	    String casebaseName, String amalFuncName, int k){

	this.k = k;

	Project project = App.getProject();
	Concept concept = project.getConceptByID(conceptName);
	ICaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebaseName);
	
	TemporaryAmalgamFctManager tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);
	
	if (amalFuncName != null && !amalFuncName.isEmpty()){
           // This will change the default Amalgamation Function of the myCBR project to a user specified functions
           try {
        	tempAmalgamFctManager.changeAmalgamFct(amalFuncName);
           } catch (TemporaryAmalgamFctNotChangedException e) {
        	e.printStackTrace();
           }
	}
	
	Collection<Instance> instances = cb.getCases();

	for(Instance instance: instances) {
	    String key = instance.getName();
	    selfSimMatrix.putIfAbsent(key, retrieve(concept, cb, key));
	}

	return selfSimMatrix;
    }

    private Map<String, Double> retrieve(Concept concept, ICaseBase casebase, String caseID) {

	Map<String, Double> resultList = new LinkedHashMap<>();

	Retrieval r = new Retrieval(concept, casebase, this);

	Instance query = r.getQueryInstance();

	Instance caze = concept.getInstance(caseID);

	for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes().entrySet()) {
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
