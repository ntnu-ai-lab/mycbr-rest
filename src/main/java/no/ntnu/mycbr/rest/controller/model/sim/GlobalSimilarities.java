package no.ntnu.mycbr.rest.controller.model.sim;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Amar Jaiswal
 */
public class GlobalSimilarities {

    Set<GlobalSimilarity> globalSims = new HashSet<>();

    public Set<GlobalSimilarity> getGlobalSims() {
        return globalSims;
    }

    public void addSim(GlobalSimilarity globalSim) {
        globalSims.add(globalSim);
    }
}
