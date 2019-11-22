package no.ntnu.mycbr.rest.service;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class SelfSimilarityRetrieval implements RetrievalCustomer{

    private final Log logger = LogFactory.getLog(getClass());

    private int k = -1;

    private List<Pair<Instance,Similarity>> results;

    private LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

    //TemporaryAmalgamFctManager tempAmalgamFctManager;

    private LinkedHashMap<String, LinkedHashMap<String, Double>> selfSimMatrix = new LinkedHashMap<>();

    public LinkedHashMap<String, LinkedHashMap<String, Double>> getCaseBaseSelfSimilarity(String casebase, 
	    String conceptName, String amalFunc, int k){

	this.k = k;

	String cbTempName = "temp_cb";

	Project project = App.getProject();
	Concept concept = project.getConceptByID(conceptName);
	ICaseBase cbFrom = (DefaultCaseBase)project.getCaseBases().get(casebase);

	int parentCBSize = cbFrom.getCases().size();

	ICaseBase tempCB = createEmptyCasebase(project, cbTempName, parentCBSize);

	transferCases(cbFrom, tempCB);

	Collection<Instance> instances = getAllInstances(tempCB);

	for(Instance instance: instances) {
	    String key = instance.getName();
	    selfSimMatrix.putIfAbsent(key, query(concept, tempCB, key));
	}

	return selfSimMatrix;
    }


    private Collection<Instance> getAllInstances(ICaseBase casebase) {
	return casebase.getCases(); 
    }

    private void transferCases(ICaseBase cbFrom, ICaseBase cbTo) {

	Collection<Instance> instances = cbFrom.getCases();

	for(Instance instance : instances) {
	    cbTo.addCase(instance);
	}
    }

    private ICaseBase createEmptyCasebase(Project project, String cbName, int caseCount) {
	ICaseBase cb = null;
	try {
	    cb = new DefaultCaseBase(project, cbName, caseCount);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return cb;
    }

    private LinkedHashMap<String, Double> query(Concept concept, ICaseBase casebase, String caseID) {

	LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

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
