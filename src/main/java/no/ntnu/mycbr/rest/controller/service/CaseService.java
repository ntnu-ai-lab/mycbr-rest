package no.ntnu.mycbr.rest.controller.service;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Attribute;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.casebase.MultipleAttribute;
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
public class CaseService {
    Project p = App.getProject();
    private final Log logger = LogFactory.getLog(getClass());

    public Instance addInstance(Concept c, ICaseBase cb, String caseID, JSONObject inpcase){
        Set keySet = inpcase.keySet();
        Instance instance = new Instance(c, caseID);

        try {
            for(Object key : keySet) {
                String strKey = (String) key;

                AttributeDesc attributeDesc = c.getAllAttributeDescs().get(strKey);

                if (attributeDesc.isMultiple()) {
                   LinkedList<Attribute> attLL = new LinkedList<Attribute>();
                   AttributeDesc aSym = c.getAttributeDesc(strKey);

                    StringTokenizer st = new StringTokenizer(inpcase.get(key).toString(), ",");

                    while (st.hasMoreElements()){
                        String symbolName = st.nextElement().toString().trim();
                        attLL.add(aSym.getAttribute(symbolName));
                    }
                    MultipleAttribute<AttributeDesc> multiSymbol = new MultipleAttribute<>(aSym, attLL);
                    instance.addAttribute(aSym, multiSymbol);
                } else {
                    instance.addAttribute(attributeDesc, inpcase.get(key));
                }
            }
        }
        catch (java.text.ParseException e) {
            logger.error("could not add instance ",e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
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

    public ArrayList<String> addInstances(Concept c, String casebaseID, Set<Map<AttributeDesc, String>> inpcases, ArrayList<String> caseIdList){

        ICaseBase cb = p.getCaseBases().get(casebaseID);
        int counter = 0;
        List<HashMap<String,String>> newCases = new ArrayList<>();
        //String idPrefix = c.getName() + "-" + casebaseID;
        ArrayList<String> ret = new ArrayList<>();
        ArrayList<Instance> newInstances = new ArrayList<>();
        Instance instance = null;
        try {
            for (Map<AttributeDesc, String> caseData : inpcases) {
                String id = caseIdList.get(counter);
                counter++;
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
        ArrayList<String> caseIdList = extractCaseIdsFromJSON(inpcases);
        return  addInstances(c,casebaseID,convertJSONArray(c,inpcases), caseIdList);
    }
    public LinkedHashSet<Map<AttributeDesc, String>> convertJSONArray(Concept c, JSONArray inpcases){
        LinkedHashSet<Map<AttributeDesc, String>> ret = new LinkedHashSet<>();
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
                if(!key.equals("id")){
                    AttributeDesc attributeDesc = c.getAllAttributeDescs().get( key);
                    values.put(attributeDesc,input);
                }

            }
            ret.add(values);
        }

        return ret;
    }

    public ArrayList<String> extractCaseIdsFromJSON(JSONArray inpcases){
        ArrayList<String> caseIdList = new ArrayList<>();

        Iterator<JSONObject> it = inpcases.iterator();

        while (it.hasNext()) {
            JSONObject ob = it.next();
            HashMap<AttributeDesc, String> values = new HashMap<>();
            caseIdList.add(ob.get("id").toString());

        }

        return caseIdList;
    }


}
