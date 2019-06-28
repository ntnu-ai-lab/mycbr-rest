package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import no.ntnu.mycbr.rest.service.InstanceService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import no.ntnu.mycbr.rest.controller.utils.TestSetup;
import static no.ntnu.mycbr.rest.controller.utils.TestSetup.*;
import static no.ntnu.mycbr.rest.common.CommonConstants.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class InstanceControllerIntegrationTest {

    private static final String TEST_CASE_ID = "car1";
    private final Log logger = LogFactory.getLog(getClass());
    private static final String INSTANCE = "{\"cases\":[{\""+ATT_DOUBLE_1+"\":0.3},{\""+ATT_DOUBLE_2+"\":0.5}]}";


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
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", INSTANCE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // check if it exists(last ".andExpect" in the code under might be wrong, but method itself does not work)
        mockMvc.perform(get(PATH_CONCEPT +CONCEPT_NAME+ PATH_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).value("0.3")));
        logger.debug("results..");

    }
    @Test
    public void addInstanceJSONTest() throws Exception {
        // add an instance
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", "{" +
                       "\"" + ATT_DOUBLE_1 +"\""+ ":0.7,"+
                       "\"" + ATT_DOUBLE_2 +"\""+ ":0.4"+
                       "}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        // check if it exists
        // todo: use getInstance instead of getAllInstancesInCaseBase
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).value("0.7")))
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_2).value("0.4")))
                .andExpect((jsonPath(JSON_PATH+"caseID").value("car1"))
        );
    }
    @Test
    public void deleteInstanceTest() throws Exception {
        // add an instance: "car1"
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", "{" +
                        "\"" + ATT_DOUBLE_1 +"\""+ ":0.7,"+
                        "\"" + ATT_DOUBLE_2 +"\""+ ":0.4"+
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // delete the instance: "car1"
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME+ PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));


    }

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        logger.info(conceptService);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        TestSetup ts = new TestSetup();
        ts.createTestCaseBase(conceptService, caseBaseService);

    }
}
