package no.ntnu.mycbr.rest.utils;

import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.retrieval.Retrieval;
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCustomer implements Retrieval.RetrievalCustomer {
    ConcurrentHashMap<String,List<Pair<Instance,Similarity>>> cResults;
    ConcurrentHashMap<Retrieval,String> retrieverMap;
    public ConcurrentCustomer(){
        cResults = new ConcurrentHashMap<>();
        retrieverMap = new ConcurrentHashMap<>();
    }
    @Override
    public void addResults(Retrieval ret, List<Pair<Instance, Similarity>> results) {
        if(retrieverMap.containsKey(ret)){
            cResults.put(retrieverMap.get(ret),results);
        }
    }
    public ConcurrentHashMap<String,List<Pair<Instance,Similarity>>> getResults(){
        return cResults;
    }
    public void addRetriever(Retrieval ret, String s){
        retrieverMap.put(ret,s);
    }
}