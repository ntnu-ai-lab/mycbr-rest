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
    private static final String INSTANCE = "{\"cases\":[{\""+ATT_DOUBLE_1+"\":0.3},{\""+ATT_DOUBLE_2+"\":0.5}]}";

    private final Log logger = LogFactory.getLog(getClass());

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private CaseBaseService caseBaseService;

    @Autowired
    private InstanceService instanceService;

    @Test
    public void addInstancesJSON() throws Exception {
        // add an instance
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", INSTANCE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // check if it exists
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
        // check if it exists with method: "getAllInstancesInCaseBase"
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).value("0.7")))
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_2).value("0.4")))
                .andExpect((jsonPath(JSON_PATH+"caseID").value("car1")));
    }

    @Test
    public void getInstanceTest() throws Exception {
        // add an instance
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", "{" +
                        "\"" + ATT_DOUBLE_1 + "\"" + ":0.7," +
                        "\"" + ATT_DOUBLE_2 + "\"" + ":0.4" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // check if it exists with method: "getInstance"
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH + ATT_DOUBLE_1).value("0.7")))
                .andExpect((jsonPath(JSON_PATH + ATT_DOUBLE_2).value("0.4")))
                .andExpect((jsonPath(JSON_PATH + "caseID").value("car1")));
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
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void deleteInstancesTest() throws Exception {
        // add an instance: "car1"
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", "{" +
                        "\"" + ATT_DOUBLE_1 +"\""+ ":0.7,"+
                        "\"" + ATT_DOUBLE_2 +"\""+ ":0.4"+
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // delete the instance: "car1"
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_CASES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        // check if instance "car1" is deleted
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                // Code under might need improvement
                .andExpect(status().isBadRequest())
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).isEmpty()))
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_2).isEmpty()))
                .andExpect((jsonPath(JSON_PATH+"caseID").isEmpty()));
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
