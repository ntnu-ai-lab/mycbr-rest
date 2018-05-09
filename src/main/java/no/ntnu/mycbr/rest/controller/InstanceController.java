package no.ntnu.mycbr.rest.controller;

import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    // Get all instances
    @ApiOperation(value = "getAllInstances", nickname = "getAllInstances")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Case.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public List<LinkedHashMap<String, String>> getAllInstances(@PathVariable(value="conceptID") String conceptID,
                                                               @RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
            @RequestParam(value="concept name", defaultValue="Car") String concept) {
        Query query = new Query(casebase);
        System.out.println("is getallcases");
        List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
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
    @RequestMapping(method = RequestMethod.PUT, value = "/concepts/{conceptID}/casebases/{casebaseID}/instances", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean addInstancesJSON(
            @RequestParam(value="JSON data", defaultValue="{}") String jsonData,
            @RequestParam(value="caseBase",defaultValue="cb") String caseBase,
            @RequestParam(value="concept" ,defaultValue = "concept" ) String concept) {
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(caseBase)){
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(caseBase);
        Concept c = (Concept)p.getSubConcepts().get(concept);

        int counter = c.getDirectInstances().size();

        Instance i = null;
        cb.addCase(i);

        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray inpcases = (JSONArray) json.get("cases");
        Iterator<JSONObject>  it = inpcases.iterator();
        List<HashMap<String,String>> newCases = new ArrayList<>();
        try {
            while (it.hasNext()) {
                JSONObject ob = it.next();
                Instance instance = null;
                HashMap<String, String> values = new HashMap<>();
                instance = new Instance(c, (String) ob.get("caseID"));
                for (Object key : ob.keySet()) {
                    values.put((String) key, (String) ob.get(key));
                    AttributeDesc attributeDesc = c.getAllAttributeDescs().get((String) key);
                    instance.addAttribute(attributeDesc, ob.get(key));
                }
                newCases.add(values);
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //cb.getProject()
        return true;
    }


}
