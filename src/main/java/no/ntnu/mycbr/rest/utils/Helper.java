package no.ntnu.mycbr.rest.utils;

import static no.ntnu.mycbr.rest.utils.Constants.*;

import no.ntnu.mycbr.core.casebase.*;
import no.ntnu.mycbr.core.model.AttributeDesc;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Helper {

    public static TreeMap<String, String> getSortedCaseContent(Instance aInstance) {
        HashMap<AttributeDesc, Attribute> atts = aInstance.getAttributes();
        TreeMap<String, String> sortedCaseContent = new TreeMap<String, String>();

        for(Map.Entry<AttributeDesc, Attribute> entry : atts.entrySet()) {
            AttributeDesc attDesc = entry.getKey();
            Attribute att = entry.getValue();
            String value = att.getValueAsString();
            value.trim();
            if (value.compareTo(UNDEFINED) != 0){
                if (value.compareTo(EMPTY) != 0){
                    sortedCaseContent.put(attDesc.getName(), att.getValueAsString());
                }
            }
        }

        return sortedCaseContent;
    }

}
