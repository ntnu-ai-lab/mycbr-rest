package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import no.ntnu.mycbr.rest.service.InstanceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static no.ntnu.mycbr.rest.common.CommonConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class CaseBaseControllerIntegrationTest {

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

    // test for creating a single case base
    @Test
    public void createCaseBaseTest() throws Exception {
        // create a Case Base
        mockMvc.perform(put(PATH_CASEBASE + CASE_BASE_NAME)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // check that Case Base is created
        Assertions.assertTrue(isCaseBaseCreated(), "Case Base is not created correctly");
    }

    // test for creating two case bases
    @Test
    public void createTwoCaseBasesTest() throws Exception {
        // create a case base
        Assertions.assertTrue(createCaseBase(), "Case base was not created correctly");

        // create a second case base
        mockMvc.perform(put(PATH_CASEBASE + CASE_BASE_NAME_SECOND)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // check that both case bases exist
        mockMvc.perform(get(PATH_CASEBASE_SINGULAR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.caseBases.[0]")).value( CASE_BASE_NAME ))
                .andExpect((jsonPath("$.caseBases.[1]")).value( CASE_BASE_NAME_SECOND ));
    }

    // test for getting a case base when one case base exist
    @Test
    public void getCaseBasesTest() throws Exception {
        // create a case base
        Assertions.assertTrue(createCaseBase(), "Case base was not created correctly");

        // check if the case base exists
        mockMvc.perform(get(PATH_CASEBASE_SINGULAR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.caseBases")).value( CASE_BASE_NAME ));
    }

    // test for getting a case base when no case base exist
    @Test
    public void getEmptyCaseBaseTest() throws Exception {
        // confirm that there exist no case base
        Assertions.assertFalse(isACaseBaseExisting(), "There exists a case base when it should not");

        // try to get a case base and expect none to be returned
        mockMvc.perform(get(PATH_CASEBASE_SINGULAR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.caseBases")).isEmpty());
    }

    // test for deleting a case base after it is added
    @Test
    public void deleteCaseBaseTest() throws Exception {
        // confirm that there exist no case base
        Assertions.assertFalse(isACaseBaseExisting(), "There exists a case base when it should not");

        // create a case base
        Assertions.assertTrue(createCaseBase(), "Case base was not created correctly");

        // delete the case base
        mockMvc.perform(delete(PATH_CASEBASE + CASE_BASE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // confirm that there exist no case base
        Assertions.assertFalse(isACaseBaseExisting(), "There exists a case base when it should not");
    }

    // test for deleting a case base when no case base exists
    @Test
    public void deleteEmptyCaseBaseTest() throws Exception {
        // confirm that there exist no case base
        Assertions.assertFalse(isACaseBaseExisting(), "There exists a case base when it should not");

        // try to delete the case base
        mockMvc.perform(delete(PATH_CASEBASE + CASE_BASE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // confirm that there exist no case base
        Assertions.assertFalse(isACaseBaseExisting(), "There exists a case base when it should not");
    }

    private boolean createCaseBase() {
        // create a CaseBase
        boolean flag = false;
        try {
            mockMvc.perform(put(PATH_CASEBASE + CASE_BASE_NAME)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean isACaseBaseExisting() {
        boolean flag = true;
        try {
            mockMvc.perform(get(PATH_CASEBASE_SINGULAR)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect((jsonPath("$.caseBases")).isEmpty());
            flag = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean isCaseBaseCreated() {
        boolean flag = false;
        try {
            mockMvc.perform(get(PATH_CASEBASE_SINGULAR)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect((jsonPath("$.caseBases")).value( CASE_BASE_NAME ));
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        conceptService.deleteAllConcepts();
    }

    @After
    public void after() throws Exception {
        mockMvc.perform(delete(PATH_CASEBASE + CASE_BASE_NAME)
                .contentType(MediaType.APPLICATION_JSON));
    }
}