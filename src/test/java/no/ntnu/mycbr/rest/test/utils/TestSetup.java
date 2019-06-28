package no.ntnu.mycbr.rest.test.utils;

import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.test.InstanceControllerIntegrationTest;

import static no.ntnu.mycbr.rest.test.InstanceControllerIntegrationTest.AMALGAMATION_FUNC;
private static final String DOUBLEATT_1 = "doubleatt_1";
private static final String DOUBLEATT_2 = "doubleatt_2";
import static no.ntnu.mycbr.rest.utils.Constants.SIMILARITY;

public class TestSetup {

    private static final String TEST_CONCEPT_NAME = "cars";
    private static final String TEST_CASEBASE_NAME = "carCB";
    private static final String DOUBLEATT_1 = "doubleatt_1";
    private static final String DOUBLEATT_2 = "doubleatt_2";
    private static final String SIMILARITY = "sim";
    private static final String AMALGAMATION_FUNC = "amalgamationFunc";
    
     conceptService.deleteAllConcepts();
    Concept c = conceptService.addConcept(TestSetup.TEST_CONCEPT_NAME);
        try


    {
        conceptService.addDoubleAttribute(c, DOUBLEATT_1,0,1,false);
        conceptService.addDoubleAttribute(c, DOUBLEATT_2,0,1,false);

        caseBaseService.addCaseBase(TestSetup.TEST_CASEBASE_NAME);

        conceptService.addDoubleSimilarityFunction(c, DOUBLEATT_1,DOUBLEATT_1+SIMILARITY,0.5);
        conceptService.addDoubleSimilarityFunction(c, DOUBLEATT_2,DOUBLEATT_2+SIMILARITY,0.5);

        conceptService.addAmalgamationFunction(c,AMALGAMATION_FUNC, "EUCLIDEAN");

    } catch (Exception e) {
        logger.error("got exception during setup of instance tests", e);
    }
}
