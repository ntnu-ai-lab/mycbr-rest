package no.ntnu.mycbr.rest.controller;

import java.util.*;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.Case;
import no.ntnu.mycbr.rest.Query;
import no.ntnu.mycbr.rest.ValueRange;
import no.ntnu.mycbr.rest.service.InstanceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static no.ntnu.mycbr.rest.utils.QueryUtils.getFullResult;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.Constants.*;

@RestController
public class InstanceController
{
    @Autowired
    private InstanceService instanceService;

    private final Log logger = LogFactory.getLog(getClass());

    //Get one instance
    @ApiOperation(value = "getInstance", nickname = "getInstance")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{instanceID}", headers="Accept=application/json")
    @ApiResponsesForCase
    public Case getInstance(@PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @PathVariable(value=INSTANCE_ID_STR) String instanceID) {
	return new Case(instanceID);
    }

    //Delete one instance
    @ApiOperation(value = "deleteInstance", nickname = "deleteInstance")
    @RequestMapping(method = RequestMethod.DELETE, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{instanceID}", headers="Accept=application/json")
    @ApiResponsesDefault
    public boolean deleteInstance(@PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @PathVariable(value=INSTANCE_ID_STR) String instanceID) {
	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(casebaseID))
	    return false;
	ICaseBase cb = p.getCaseBases().get(casebaseID);
	if(cb.containsCase(casebaseID)==null)
	    return false;
	p.getCaseBases().get(casebaseID).removeCase(casebaseID);
	return true;
    }

    // Get all instances in case base of a concept
    @ApiOperation(value = "getAllInstances", nickname = "getAllInstances")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/instances", headers="Accept=application/json")
    @ApiResponsesDefault
    public Collection<Case> getAllInstances(@PathVariable(value=CONCEPT_ID_STR) String conceptID) {
	Project p = App.getProject();

	/*Query query = new Query(conceptID);

        System.out.println("p get all instances size: "+p.getAllInstances().size());
        //System.out.println("is getallcases"+App.getProject().getSuperConcept().getAllInstances().size());
        //System.out.println("is getallcases"+App.getProject().getSuperConcept().getAllInstances().size());
        List<Instance> instances = new ArrayList<>();
        for(ICaseBase iCaseBase : p.getCaseBases().values()){
            logger.info("casebase has "+iCaseBase.getCases().size()+" cases ");
            instances.addAll(iCaseBase.getCases());
        }*/
	Collection<Instance> instances = p.getAllInstances();
	Collection<Case> ret = new LinkedList<>();
	for(Instance instance : instances){
	    if(instance.getConcept().getName().contentEquals(conceptID))
		ret.add(new Case(instance.getName(),conceptID));
	}
	return ret;
    }

    // Get all instances of a concept
    @ApiOperation(value = "getAllInstancesInCaseBase", nickname = "getAllInstancesInCaseBase")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances", headers="Accept=application/json")
    @ApiResponsesForCase
    public List<LinkedHashMap<String, String>> getAllInstancesInCaseBase(@PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID) {
	Query query = new Query(casebaseID,conceptID);
	//TODO: filter to one type of concept
	List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
	return cases;
    }


    //Delete all instances
    @ApiOperation(value="deleteInstances", nickname="deleteInstances")
    @RequestMapping(method=RequestMethod.DELETE, value = "/concepts/{conceptID}/casebases/{casebaseID}/cases")
    @ApiResponsesForValueRange
    public boolean deleteInstances(@PathVariable(value="concept") String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID){

	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(casebaseID))
	    return false;
	Collection<Instance> collection = p.getCaseBases().get(casebaseID).getCases();
	for(Instance i : collection){
	    collection.remove(i);
	}

	return true;
    }


    //Delete instances according to pattern
    @ApiOperation(value="deleteInstances", nickname="deleteInstancePattern")
    @RequestMapping(method=RequestMethod.DELETE, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/delete")
    @ApiResponsesForValueRange
    public boolean deleteInstancePattern(@PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value="caseBase") String caseBase,
	    @RequestParam(value="pattern",defaultValue="*") String pattern
	    ){

	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(caseBase))
	    return false;
	if(pattern.contentEquals("*")){//this means delete all
	    Collection<Instance> collection = p.getCaseBases().get(caseBase).getCases();
	    Iterator<Instance> it = collection.iterator();
	    while(it.hasNext()){
		p.removeCase(it.next().getName());
	    }
	    p.save();
	}else{
	    Collection<Instance> collection = p.getCaseBases().get(caseBase).getCases();
	    for(Instance i : collection){
		collection.remove(i);
	    }

	}
	return true;
    }

    /*
    Add instances
    input should be:
    {cases:
    [
    {id:"caseid0",otherattribute:value,..}
    {id:"caseid1",otherattribute:value,..}
    ]
    }
     */
    @ApiOperation(value = "addInstancesJSON", nickname = "addInstancesJSON")
    @RequestMapping(method = RequestMethod.POST, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances", headers="Accept=application/json")
    @ApiResponsesDefault
    public ArrayList<String> addInstancesJSON(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @RequestParam(value="cases", defaultValue="{'cases':[{'Att':'Value'},{'Att':'Value'}]}") String cases
	    ) {
	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(casebaseID)){
	    return new ArrayList<>();
	}
	Concept c = (Concept)p.getSubConcepts().get(conceptID);

	JSONParser parser = new JSONParser();
	JSONObject json = null;
	try {
	    json = (JSONObject) parser.parse(cases);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	JSONArray inpcases = (JSONArray) json.get("cases");

	return instanceService.addInstances(c,casebaseID,inpcases);
    }
    @ApiOperation(value = "addInstanceJSON", nickname = "addInstancesJSON")
    @RequestMapping(method = RequestMethod.PUT, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{caseID}", headers="Accept=application/json")
    @ApiResponsesDefault
    public boolean addInstanceJSON(
	    @PathVariable(value=CONCEPT_ID_STR) String conceptID,
	    @PathVariable(value=CASEBASE_ID_STR) String casebaseID,
	    @PathVariable(value=CASE_ID_STR) String caseID,
	    @RequestParam(value="casedata", defaultValue="{}") String casedata
	    ) {
	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(casebaseID)){
	    return false;
	}
	ICaseBase cb = p.getCaseBases().get(casebaseID);
	Concept c = (Concept)p.getSubConcepts().get(conceptID);

	JSONParser parser = new JSONParser();
	JSONObject json = null;
	try {
	    json = (JSONObject) parser.parse(casedata);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	JSONObject inpcase = json;
	return null != instanceService.addInstance(c,cb,caseID,inpcase);

    }

}
