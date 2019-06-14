package no.ntnu.mycbr.rest.service;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.App;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InstanceService {
    Project p = App.getProject();
    private final Log logger = LogFactory.getLog(getClass());

    public Instance addInstance(Concept c, ICaseBase cb, String caseID, JSONObject inpcase){
        Set keySet = inpcase.keySet();
        Instance instance = new Instance(c, caseID);

        try {
            for(Object key : keySet) {
                String strKey = (String) key;

                AttributeDesc attributeDesc = c.getAllAttributeDescs().get(strKey);
                instance.addAttribute(attributeDesc, inpcase.get(key));
            }
        }
        catch (java.text.ParseException e) {
            logger.error("could not add instance ",e);
            return null;
        }

        cb.addCase(instance);
        AmalgamationFct afct = c.getActiveAmalgamFct();
        if(afct.getType() == AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY){
            ArrayList<Instance> instances = new ArrayList<>();
            instances.add(instance);
            afct.cacheNeuralSims(instances);
        }

        return instance;
    }

    public ArrayList<String> addInstances(Concept c, String casebaseID, Set<Map<AttributeDesc, String>> inpcases){
        ICaseBase cb = p.getCaseBases().get(casebaseID);
        int counter = c.getDirectInstances().size();
        List<HashMap<String,String>> newCases = new ArrayList<>();
        String idPrefix = c.getName() + "-" + casebaseID;
        ArrayList<String> ret = new ArrayList<>();
        ArrayList<Instance> newInstances = new ArrayList<>();
        Instance instance = null;
        try {
            for (Map<AttributeDesc, String> caseData : inpcases) {
                counter++;
                String id = idPrefix + Integer.toString(counter);
                ret.add(id);
                instance = new Instance(c, id);
                for (AttributeDesc attributeDesc : caseData.keySet()) {
                    instance.addAttribute(attributeDesc, caseData.get(attributeDesc));
                }
                cb.addCase(instance);
                newInstances.add(instance);
            }
            AmalgamationFct afct = c.getActiveAmalgamFct();
            if(afct.getType() == AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY){
                afct.cacheNeuralSims(newInstances);
            }
        } catch (Exception e) {
            logger.error("got exception while trying to add instances",e);
            return null;
        }
        return ret;
    }
    public ArrayList<String> addInstances(Concept c, String casebaseID, JSONArray inpcases){
        return  addInstances(c,casebaseID,convertJSONArray(c,inpcases));
    }
    private Set<Map<AttributeDesc, String>> convertJSONArray(Concept c, JSONArray inpcases){
        Set<Map<AttributeDesc, String>> ret = new HashSet<>();
        Iterator<JSONObject> it = inpcases.iterator();

        while (it.hasNext()) {
            JSONObject ob = it.next();
            HashMap<AttributeDesc, String> values = new HashMap<>();
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

                AttributeDesc attributeDesc = c.getAllAttributeDescs().get( key);
                values.put(attributeDesc,input);
            }
        }

        return ret;
    }


}
