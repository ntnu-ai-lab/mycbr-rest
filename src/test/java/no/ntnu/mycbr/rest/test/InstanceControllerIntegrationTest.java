package no.ntnu.mycbr.rest.test;

import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import no.ntnu.mycbr.rest.service.InstanceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class InstanceControllerIntegrationTest {

    private static final String TEST_CONCEPT_NAME = "cars";
    private static final String TEST_CASEBASE_NAME = "carCB";
    private static final String DOUBLEATT_1 = "doubleatt_1";
    private static final String DOUBLEATT_2 = "doubleatt_2";
    private static final String AMALGAMATION_FUNC = "amalgamationFunc";
    private static final String SIMILARITY = "sim";
    private static final String TEST_CASE_ID = "car1";
    private final Log logger = LogFactory.getLog(getClass());
    private static final String INSTANCE = "{\"cases\":[{\"doubleatt_1\":0.3},{\"doubleatt_2\":0.5}]}";

    private MockMvc mockMvc;

    private ArrayList<String> caseIDs;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private CaseBaseService caseBaseService;

    @Autowired
    private InstanceService instanceService;

    // the method addInstancesJSON does not seem to work properly, since an empty string is returned when the getter is called
    @Test
    public void addInstancesJSON() throws Exception {
        // add an instance
        mockMvc.perform(post("/concepts/" + InstanceControllerIntegrationTest.TEST_CONCEPT_NAME + "/casebases/"
        + InstanceControllerIntegrationTest.TEST_CASEBASE_NAME + "/instances/")
                .param("cases", InstanceControllerIntegrationTest.INSTANCE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // check if it exists(last .andExpect in the code under might be wrong, but method itself does not work)
        mockMvc.perform(get("/concepts/"+InstanceControllerIntegrationTest.TEST_CONCEPT_NAME+"/instances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..", Matchers.hasKey("doubleatt_1")));
        logger.debug("results..");

    }
    @Test
    public void addInstanceJSONTest() throws Exception {
        // add an instance
        mockMvc.perform(put("/concepts/" + InstanceControllerIntegrationTest.TEST_CONCEPT_NAME + "/casebases/"
        + InstanceControllerIntegrationTest.TEST_CASEBASE_NAME + "/instances/" + InstanceControllerIntegrationTest.TEST_CASE_ID)
                .param("casedata", "{" +
                       "\"" + InstanceControllerIntegrationTest.DOUBLEATT_1 +"\""+ ":0.7,"+
                       "\"" + InstanceControllerIntegrationTest.DOUBLEATT_2 +"\""+ ":0.4"+
                       "}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        // check if it exists
        // todo: use getInstance instead of getAllInstancesInCaseBase
        mockMvc.perform(get("/concepts/" + InstanceControllerIntegrationTest.TEST_CONCEPT_NAME + "/casebases/" +
                InstanceControllerIntegrationTest.TEST_CASEBASE_NAME + "/instances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.."+InstanceControllerIntegrationTest.DOUBLEATT_1).value("0.7")))
                .andExpect((jsonPath("$.."+InstanceControllerIntegrationTest.DOUBLEATT_2).value("0.4")))
                .andExpect((jsonPath("$..caseID").value("car1"))
        );
    }
    @Test
    public void deleteInstanceTest() throws Exception {
        // add an instance: "car1"
        mockMvc.perform(put("/concepts/" + InstanceControllerIntegrationTest.TEST_CONCEPT_NAME + "/casebases/"
                + InstanceControllerIntegrationTest.TEST_CASEBASE_NAME + "/instances/" + InstanceControllerIntegrationTest.TEST_CASE_ID)
                .param("casedata", "{" +
                        "\"" + InstanceControllerIntegrationTest.DOUBLEATT_1 +"\""+ ":0.7,"+
                        "\"" + InstanceControllerIntegrationTest.DOUBLEATT_2 +"\""+ ":0.4"+
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // delete the instance: "car1"
        mockMvc.perform(delete("/concepts/" + InstanceControllerIntegrationTest.TEST_CONCEPT_NAME + "/casebases/" +
                InstanceControllerIntegrationTest.TEST_CASEBASE_NAME+ "/instances/" + InstanceControllerIntegrationTest.TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));


    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();


    }
}
