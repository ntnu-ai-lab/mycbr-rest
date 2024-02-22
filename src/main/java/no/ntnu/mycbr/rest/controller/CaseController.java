package no.ntnu.mycbr.rest.controller;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.controller.helper.Case;
import no.ntnu.mycbr.rest.controller.helper.Query;
import no.ntnu.mycbr.rest.controller.service.CaseService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;
import static no.ntnu.mycbr.rest.utils.QueryUtils.getFullResult;

@RestController
public class CaseController {
    private static final String CASES_BY_PATTERN = "/casesByPattern";

    @Autowired
    private CaseService instanceService;

    private final Log logger = LogFactory.getLog(getClass());

    //Get one instance
    @ApiOperation(value = GET_CASE_BY_CASE_ID, nickname = GET_CASE_BY_CASE_ID)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_CASEBASE_CASE_ID,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, String> getInstance(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID,
            @PathVariable(value = CASE_ID) String caseID) {

        Project p = App.getProject();

        if (!p.getCaseBases().containsKey(casebaseID))
            return null;

        Instance instance = p.getInstance(caseID);

        Case queriedCase = new Case(instance.getName(), conceptID);

        return queriedCase.getCase();
    }

    //Delete one instance
    @ApiOperation(value = DELETE_CASE_BY_CASE_ID, nickname = DELETE_CASE_BY_CASE_ID)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_CASEBASE_CASE_ID,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteInstance(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID,
            @PathVariable(value = CASE_ID) String caseID) {
        Project p = App.getProject();
        if (!p.getCaseBases().containsKey(casebaseID)) {
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        if (cb.containsCase(caseID) == null) {
            return false;
        } else {
            p.getCaseBases().get(casebaseID).removeCase(caseID);
            p.save();
            return true;
        }
    }

    // Get all instances in case base of a concept
    @ApiOperation(value = GET_ALL_CASES, nickname = GET_ALL_CASES)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_CASES, headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public List<Map<String, String>> getAllInstances(
            @PathVariable(value = CONCEPT_ID) String conceptID) {

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
        List<Map<String, String>> ret = new LinkedList<>();
        for (Instance instance : instances) {
            if (instance.getConcept().getName().contentEquals(conceptID))
                ret.add(new Case(instance.getName(), conceptID).getCase());
        }
        return ret;
    }


    // Deletes all instances from a concept
    @ApiOperation(value = DELETE_ALL_CASES, nickname = DELETE_ALL_CASES)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_CASES, headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAllInstances(
            @PathVariable(value = CONCEPT_ID) String conceptID) {

        Project p = App.getProject();
        Collection<Instance> instances = p.getAllInstances();

        for (Instance instance : instances) {
            if (instance.getConcept().getName().contentEquals(conceptID))
                p.getConceptByID(conceptID).removeInstance(instance.getName());
        }
        p.save();
        System.out.println("Deleted all instances of concept: " + conceptID);
        return true;
    }

    // Get all instances of a concept
    @ApiOperation(value = GET_ALL_CASES_FROM_CASEBASE, nickname = GET_ALL_CASES_FROM_CASEBASE)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_CASEBASE_CASES,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public List<LinkedHashMap<String, String>> getAllInstancesInCaseBase(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID) {

        Query query = new Query(casebaseID, conceptID);
        //TODO: filter to one type of concept
        List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
        return cases;
    }


    //Delete all cases from a casebase
    @ApiOperation(value = DELETE_ALL_CASES_FROM_CB, nickname = DELETE_ALL_CASES_FROM_CB)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_CASEBASE_CASES)
    @ApiResponsesDefault
    public boolean deleteInstances(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID) {

        Project p = App.getProject();
        p.save();
        if (!p.getCaseBases().containsKey(casebaseID))
            return false;
        Collection<Instance> collection = p.getCaseBases().get(casebaseID).getCases();
        for (Instance i : collection) {
            p.getCaseBases().get(casebaseID).removeCase(i.getName());
        }
        System.out.println("Current project: " + p.getProject().getName());
        p.getProject().save();
        System.out.println("Deleted all instances of casebase: " + casebaseID);
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
    @ApiOperation(value = ADD_MULTIPLE_CASES_USING_JSON, nickname = ADD_MULTIPLE_CASES_USING_JSON)
    @RequestMapping(method = RequestMethod.POST, value = PATH_CONCEPT_CASEBASE_CASES,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public ArrayList<String> addInstancesJSON(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID,
            @RequestBody(required = true) JSONObject json) {

        Project p = App.getProject();
        if (!p.getCaseBases().containsKey(casebaseID)) {
            return new ArrayList<>();
        }
        Concept c = (Concept) p.getSubConcepts().get(conceptID);

        JSONObject obj = (JSONObject) JSONValue.parse(String.valueOf(json));
        JSONArray newCases = (JSONArray) obj.get(CASES);

        return instanceService.addInstances(c, casebaseID, newCases);
    }

    // This call adds a new case; if the ID exists, it does not add anything
    @ApiOperation(value = ADD_CASE_USING_JSON, nickname = ADD_CASE_USING_JSON)
    @RequestMapping(method = RequestMethod.POST, value = PATH_CONCEPT_CASEBASE_CASE_ID,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean addInstanceJSON(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID,
            @PathVariable(value = CASE_ID) String caseID,
            @RequestBody(required = true) JSONObject json) {

        Project p = App.getProject();
        if (!p.getCaseBases().containsKey(casebaseID)) {
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        Concept c = (Concept) p.getSubConcepts().get(conceptID);

        return null != instanceService.addInstance(c, cb, caseID, json);
    }

    // this call updates existing cases
    @ApiOperation(value = UPDATE_CASE_USING_JSON, nickname = UPDATE_CASE_USING_JSON)
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_CASEBASE_CASE_ID,
            headers = ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean updateInstanceJSON(
            @PathVariable(value = CONCEPT_ID) String conceptID,
            @PathVariable(value = CASEBASE_ID) String casebaseID,
            @PathVariable(value = CASE_ID) String caseID,
            @RequestBody(required = true) JSONObject json) {

        Project p = App.getProject();
        if (!p.getCaseBases().containsKey(casebaseID)) {
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        Concept c = (Concept) p.getSubConcepts().get(conceptID);

        // check if the case exists and delete it
        if (p.getCaseBases().get(casebaseID).getCases().contains(p.getInstance(caseID))) {
            p.getSubConcepts().get(conceptID).removeInstance(caseID);
        } else  // if the case does not exist don't do anything
            return false;
        // add the case from the provided json
        return null != instanceService.addInstance(c, cb, caseID, json);
    }

}
