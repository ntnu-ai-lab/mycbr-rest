package no.ntnu.mycbr.rest.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.ConceptController;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import no.ntnu.mycbr.rest.service.InstanceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class RetrievalControllerIntegrationTest {

    public static final String DOUBLEATT_1 = "doubleatt1";
    public static final String DOUBLEATT_2 = "doubleatt2";
    public static final String DOUBLEATT_3 = "doubleatt3";
    private static final String AMALGAMATION_FUNC1 = "amalgamationFunc1";
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private CaseBaseService caseBaseService;

    private ArrayList<String> caseIDs;

    private MockMvc mockMvc;

    private static String testcasebaseid = "testCaseBaseID";

    private static String conceptID = "testConcept";

    @Autowired
    WebApplicationContext wac;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testSimpleRetrieve()
            throws Exception {

        String casesArray = "[\""+caseIDs.get(0)+"\"";
        for(int i = 1; i<caseIDs.size();i++)
            casesArray += ",\""+caseIDs.get(i)+"\"";
        casesArray += "]";

        // do retrieval test for getting a matrix of sims back.. should run in parallell..
        MockHttpServletRequestBuilder call = get("/concepts/"+RetrievalControllerIntegrationTest.conceptID
                +"/casebases/"+RetrievalControllerIntegrationTest.testcasebaseid+"/retrievalByIDsInIDs")
                .param("caseIDs",casesArray)
                .param("filterCaseIDs",casesArray);
        ResultActions res = mockMvc.perform(call
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        logger.debug("results..");
    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        conceptService.deleteAllConcepts();
        Concept c = conceptService.addConcept(RetrievalControllerIntegrationTest.conceptID);
        try {
            conceptService.addDoubleAttribute(c, DOUBLEATT_1, 0, 1, false);
            conceptService.addDoubleAttribute(c, DOUBLEATT_2, 0, 1, false);
            conceptService.addDoubleAttribute(c, DOUBLEATT_3, 0, 1, false);

            caseBaseService.addCaseBase(RetrievalControllerIntegrationTest.testcasebaseid);

            // create a set for new instance data
            Set<Map<AttributeDesc,String>> instances = new HashSet<>();

            // data for instance 1
            instances.add(new HashMap<AttributeDesc, String>() {{
                put(c.getAttributeDesc(DOUBLEATT_1), "1.0");
                put(c.getAttributeDesc(DOUBLEATT_2), "1.0");
                put(c.getAttributeDesc(DOUBLEATT_3), "1.0");
            }});

            // data for instance 2
            instances.add(new HashMap<AttributeDesc, String>() {{
                put(c.getAttributeDesc(DOUBLEATT_1), "0.5");
                put(c.getAttributeDesc(DOUBLEATT_2), "0.5");
                put(c.getAttributeDesc(DOUBLEATT_3), "0.5");
            }});

            // data for instance 3
            instances.add(new HashMap<AttributeDesc, String>() {{
                put(c.getAttributeDesc(DOUBLEATT_1), "0.1");
                put(c.getAttributeDesc(DOUBLEATT_2), "0.1");
                put(c.getAttributeDesc(DOUBLEATT_3), "0.1");
            }});

            caseIDs = instanceService.addInstances(c,RetrievalControllerIntegrationTest.testcasebaseid,instances);
            conceptService.addDoubleSimilarityFunction(c, DOUBLEATT_1,DOUBLEATT_1+"sim",0.5);
            conceptService.addDoubleSimilarityFunction(c, DOUBLEATT_2,DOUBLEATT_2+"sim",0.5);
            conceptService.addDoubleSimilarityFunction(c, DOUBLEATT_3,DOUBLEATT_3+"sim",0.5);
            conceptService.addAmalgamationFunction(c,AMALGAMATION_FUNC1, "EUCLIDEAN");

        } catch (Exception e) {
            logger.error("got exception during setup of retrieval tests",e);
        }
    }

    private byte[] toJson(Object o) throws Exception{
        return this.mapper.writeValueAsString(o).getBytes();
    }
}
