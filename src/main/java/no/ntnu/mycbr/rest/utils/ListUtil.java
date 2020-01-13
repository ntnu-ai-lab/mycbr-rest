package no.ntnu.mycbr.rest.utils;


import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class ListUtil {
    public static List<AttributeDesc> sortAttributeDescs(
            Collection<AttributeDesc> attrDescs) {
        List<AttributeDesc> result = new ArrayList<AttributeDesc>(attrDescs);
        Collections.sort(result, new Comparator<AttributeDesc>() {
            @Override
            public int compare(AttributeDesc o1, AttributeDesc o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }

    public static List<Concept> sortConcepts(Collection<Concept> concepts) {
        List<Concept> result = new ArrayList<Concept>(concepts);
        Collections.sort(result, new Comparator<Concept>() {
            @Override
            public int compare(Concept o1, Concept o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }

    public static List<String> sortSymbolAttributes(
            Collection<String> symbolNames) {
        List<String> result = new ArrayList<String>(symbolNames);
        Collections.sort(result);
        return result;
    }

    public static List<ICaseBase> sortCaseBases(Collection<ICaseBase> cb) {
        List<ICaseBase> result = new ArrayList<ICaseBase>(cb);
        Collections.sort(result, new Comparator<ICaseBase>() {
            @Override
            public int compare(ICaseBase o1, ICaseBase o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }

    public static List<Instance> sortInstances(Collection<Instance> instances) {
        List<Instance> result = new ArrayList<Instance>(instances);
        Collections.sort(result, new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }
}
