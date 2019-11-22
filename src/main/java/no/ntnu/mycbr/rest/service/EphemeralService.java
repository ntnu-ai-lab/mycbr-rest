package no.ntnu.mycbr.rest.service;

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
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class EphemeralService implements RetrievalCustomer{

    private final Log logger = LogFactory.getLog(getClass());

    private int k = -1;
    private Project project;
    private ICaseBase cb;
    private Concept concept;
    private TemporaryAmalgamFctManager tempAmalgamFctManager;

    private List<Pair<Instance,Similarity>> results;
    private LinkedHashMap<String, LinkedHashMap<String, Double>> simMatrix = new LinkedHashMap<>();

    public EphemeralService(String casebaseName, String conceptName, String amalFunc, int k) {		
	this.k = k;
	this.project = App.getProject();
	this.cb = (DefaultCaseBase)project.getCaseBases().get(casebaseName);
	this.concept = project.getConceptByID(conceptName);

	this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(concept);

	try {
	    tempAmalgamFctManager.changeAmalgamFct(amalFunc);
	} catch (TemporaryAmalgamFctNotChangedException e) {
	    e.printStackTrace();
	}
    }

    public LinkedHashMap<String, LinkedHashMap<String, Double>> ephemeralRetrival( Set<String> querySet, Set<String> cbSet) {

	String cbTempName = "temp_cb";

	ICaseBase fromCB = cb;
	ICaseBase toCB = createEmptyCasebase(cbTempName, cbSet.size());

	transferCases( cbSet, fromCB, toCB);

	for(String key: querySet) {
	    simMatrix.putIfAbsent(key, query(concept, toCB, key));
	}

	return simMatrix;
    }

    private Collection<Instance> getAllInstances(ICaseBase casebase) {
	return casebase.getCases(); 
    }

    private void transferCases(Set<String> caseIds, ICaseBase fromCB, ICaseBase toCB) {

	Collection<Instance> instances = fromCB.getCases();

	for(Instance instance : instances) {
	    if(caseIds.contains(instance.getName())) {
		toCB.addCase(instance);
	    }
	}
    }

    private void transferCases(ICaseBase fromCB, ICaseBase toCB) {

	Collection<Instance> instances = fromCB.getCases();

	for(Instance instance : instances) {
	    toCB.addCase(instance);
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

    private LinkedHashMap<String, Double> query( Concept concept, ICaseBase casebase, String caseID) {

	LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

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