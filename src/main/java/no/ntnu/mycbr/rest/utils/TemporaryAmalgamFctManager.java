package no.ntnu.mycbr.rest.utils;

import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.AmalgamationFct;

/**
 * Created by jont on 23.03.2017.
 */

public class TemporaryAmalgamFctManager {
    private Concept concept;
    private AmalgamationFct previousAmalgamationFunction;
    private boolean changed = false;

    public TemporaryAmalgamFctManager(Concept concept) {
        this.concept = concept;
        previousAmalgamationFunction = concept.getActiveAmalgamFct();
    }


    /**
     * Changes amalgram function of concept until rollBack() is called (NB!) or program exits.
     * The changes are not saved to the myCBR project, even if rollBack() is not called.
     * */
    public void changeAmalgamFct(AmalgamationFct amalgamFct) {
        concept.setActiveAmalgamFct(amalgamFct);
        changed = true;
    }

    public void changeAmalgamFct(String newAmalgamFct) throws TemporaryAmalgamFctNotChangedException {
        for(AmalgamationFct amalgamFct : concept.getAvailableAmalgamFcts()) {
            if (newAmalgamFct.contentEquals(amalgamFct.getName())) {
                changeAmalgamFct(amalgamFct);
                break;
            }
        }

        if(!changed) {
            throw new TemporaryAmalgamFctNotChangedException();
        }
    }

    public void rollBack() {
        if(changed) {
            concept.setActiveAmalgamFct(previousAmalgamationFunction);
        }
    }

    protected void finalize() throws Throwable {
        // Not guaranteed to be called. Call always rollBack() with try ... finally.

        try {
            //rollBack();
        } finally {
            super.finalize();
        }
    }
}
