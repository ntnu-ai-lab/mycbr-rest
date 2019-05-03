package no.ntnu.mycbr.rest.controller;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static no.ntnu.mycbr.rest.utils.RESTCBRUtils.getFullResult;

@RestController
public class InstanceController
{
    private final Log logger = LogFactory.getLog(getClass());

    //Get one instance
    @ApiOperation(value = "getInstance", nickname = "getInstance")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{instanceID}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Case.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Case getInstance(@PathVariable(value="conceptID") String conceptID,
                            @PathVariable(value="casebaseID") String casebaseID,
                            @PathVariable(value="instanceID") String instanceID) {
        return new Case(instanceID);
    }

    //Delete one instance
    @ApiOperation(value = "deleteInstance", nickname = "deleteInstance")
    @RequestMapping(method = RequestMethod.DELETE, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{instanceID}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteInstance(@PathVariable(value="conceptID") String conceptID,
                               @PathVariable(value="casebaseID") String casebaseID,
                               @PathVariable(value="instanceID") String instanceID) {
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Collection<Case> getAllInstances(@PathVariable(value="conceptID") String conceptID) {
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Case.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public List<LinkedHashMap<String, String>> getAllInstancesInCaseBase(@PathVariable(value="conceptID") String conceptID,
                                                               @PathVariable(value="casebaseID") String casebaseID) {
        Query query = new Query(casebaseID,conceptID);
        //TODO: filter to one type of concept
        List<LinkedHashMap<String, String>> cases = getFullResult(query, conceptID);
        return cases;
    }


    //Delete all instances
    @ApiOperation(value="deleteInstances", nickname="deleteInstances")
    @RequestMapping(method=RequestMethod.DELETE, value = "/concepts/{conceptID}/casebases/{casebaseID}/cases")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteInstances(@PathVariable(value="concept") String conceptID,
                               @PathVariable(value="casebaseID") String casebaseID){

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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteInstancePattern(@PathVariable(value="conceptID") String conceptID,
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ArrayList<String> addInstancesJSON(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @RequestParam(value="cases", defaultValue="{'cases':[{'Att':'Value'},{'Att':'Value'}]}") String cases
            ) {
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(casebaseID)){
            return new ArrayList<>();
        }
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        Concept c = (Concept)p.getSubConcepts().get(conceptID);

        int counter = c.getDirectInstances().size();

        Instance i = null;
        //cb.addCase(i);

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(cases);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray inpcases = (JSONArray) json.get("cases");
        Iterator<JSONObject>  it = inpcases.iterator();
        List<HashMap<String,String>> newCases = new ArrayList<>();
        String idPrefix = c.getName() + "-" + casebaseID;
        ArrayList<String> ret = new ArrayList<>();
        ArrayList<Instance> newInstances = new ArrayList<>();
        try {
            while (it.hasNext()) {
                counter ++;
                JSONObject ob = it.next();
                //Instance instance = c.addInstance(ob.get("caseID"));
                Instance instance = null;
                HashMap<String, String> values = new HashMap<>();
                String id = idPrefix + Integer.toString(counter);
                ret.add(id);
                instance = new Instance(c, id);
                for (Object key : ob.keySet()) {
                    Object retObj = ob.get(key);
                    String input = null;
                    if(retObj instanceof Double){
                        input = ((Double)retObj).toString();
                    }else if(retObj instanceof String) {
                        input = (String) retObj;
                    }else if(retObj instanceof  Long) {
                        input = ((Long) retObj).toString();
                    }
                    values.put((String) key,input );
                    AttributeDesc attributeDesc = c.getAllAttributeDescs().get((String) key);
                    instance.addAttribute(attributeDesc, attributeDesc.getAttribute(input));
                }
                newInstances.add(instance);
                p.getCaseBases().get(casebaseID).addCase(instance);
                newCases.add(values);
            }
            AmalgamationFct afct = c.getActiveAmalgamFct();
            if(afct.getType() == AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY){
                afct.cacheNeuralSims(newInstances);
            }

        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //cb.getProject()
        return ret;
    }
    @ApiOperation(value = "addInstanceJSON", nickname = "addInstancesJSON")
    @RequestMapping(method = RequestMethod.PUT, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances/{caseID}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean addInstanceJSON(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="casebaseID") String casebaseID,
            @PathVariable(value="caseID") String caseID,
            @RequestParam(value="casedata", defaultValue="{}") String casedata
    ) {
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(casebaseID)){
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        Concept c = (Concept)p.getSubConcepts().get(conceptID);

        Instance i = null;
        //cb.addCase(i);

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(casedata);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject inpcase = json;
        Set keySet = inpcase.keySet();
        Instance instance = new Instance(c, (String) inpcase.get("caseID"));

        try {
            for(Object key : keySet) {
                String strKey = (String) key;

                AttributeDesc attributeDesc = c.getAllAttributeDescs().get(strKey);
                instance.addAttribute(attributeDesc, inpcase.get(key));
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        cb.addCase(instance);
        AmalgamationFct afct = c.getActiveAmalgamFct();
        if(afct.getType() == AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY){
            ArrayList<Instance> instances = new ArrayList<>();
            instances.add(instance);
            afct.cacheNeuralSims(instances);
        }
        //cb.getProject()
        return true;
    }

}
