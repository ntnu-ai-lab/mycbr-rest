package no.ntnu.mycbr.rest.controller.utils;

import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class TestSetup {

    public static final String ATT_DOUBLE_1 = "attDouble1";
    public static final String ATT_DOUBLE_2 = "attDouble2";
    public static final String ATT_DOUBLE_3 = "attDouble3";
    public static final String AMALGAMATION_FUNC_1 = "amalgamationFunc1";
    private static final String SIMILARITY = "sim";
    private final Log logger = LogFactory.getLog(getClass());

    public static final String CASE_BASE_NAME = "testCaseBase";

    public static final String CONCEPT_NAME = "testConcept";

    public void createTestCaseBase(ConceptService conceptService, CaseBaseService caseBaseService) {
        //MockitoAnnotations.initMocks(this);
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        conceptService.deleteAllConcepts();
        Concept c = conceptService.addConcept(TestSetup.CONCEPT_NAME);
        try {
            conceptService.addDoubleAttribute(c, ATT_DOUBLE_1, 0, 1, false);
            conceptService.addDoubleAttribute(c, ATT_DOUBLE_2, 0, 1, false);
            conceptService.addDoubleAttribute(c, ATT_DOUBLE_3, 0, 1, false);

            caseBaseService.addCaseBase(TestSetup.CASE_BASE_NAME);

            conceptService.addDoubleSimilarityFunction(c, ATT_DOUBLE_1, ATT_DOUBLE_1 +SIMILARITY,0.5);
            conceptService.addDoubleSimilarityFunction(c, ATT_DOUBLE_2, ATT_DOUBLE_2 +SIMILARITY,0.5);
            conceptService.addDoubleSimilarityFunction(c, ATT_DOUBLE_3, ATT_DOUBLE_3 +SIMILARITY,0.5);
            conceptService.addAmalgamationFunction(c,AMALGAMATION_FUNC_1, "EUCLIDEAN");

        } catch (Exception e) {
            logger.error("got exception during setup of retrieval tests",e);
        }
    }
}
