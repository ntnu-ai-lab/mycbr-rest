package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import no.ntnu.mycbr.rest.controller.utils.TestSetupInstanceController;

import static no.ntnu.mycbr.rest.controller.utils.TestSetupInstanceController.*;
import static no.ntnu.mycbr.rest.common.CommonConstants.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class InstanceControllerIntegrationTest {

    private static final String TEST_CASE_ID = "car1";
    private static final String INSTANCES = "{\"cases\":[{\""+ATT_DOUBLE_1+"\":0.3},{\""+ATT_DOUBLE_2+"\":0.5}]}";
    private static final String INSTANCES_WITH_ERROR = "{\"cases\":[{\""+ATT_DOUBLE_1+"\":0.3},{\""+ATT_DOUBLE_2+"\":0,5}]}";
    private static final String INSTANCE = "{\"" + ATT_DOUBLE_1 +"\":0.7,\"" + ATT_DOUBLE_2 +"\":0.4}";
    private static final String INSTANCE_WITH_ERROR = "{\"" + ATT_DOUBLE_1 +"\":0.7,\"" + ATT_DOUBLE_2 +"\":0,4}";
    private static final String EMPTY_INSTANCES = "";

    private TestSetupInstanceController ts;

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

    // Test that an instance is added to the empty case base with the method addInstancesJSON
    @Test
    public void addInstancesJSONTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // add an instance
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // check if the instance exists in the case base
        Assertions.assertFalse(isCaseBaseEmpty(), "Case base should contain an instance, but is empty");
        logger.debug("results..");
    }

    // Test to add an empty case to the empty case base with the method addInstancesJSON
    @Test
    public void addEmptyInstancesJSONTest() throws Exception {
        // Confirm the case base is empty
        Project p = App.getProject();
        if (!p.hasCB(CASE_BASE_NAME)) {
            throw new IllegalArgumentException("Case base is not empty"); }

        // add an empty instance. Expect HTTP error 422 "Unprocessable Entity"
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", EMPTY_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        // check that nothing has been added to the Case Base
        if (!p.hasCB(CASE_BASE_NAME)) {
            throw new IllegalArgumentException("Case base is not empty"); }
        logger.debug("results..");
    }

    // Test that an instance is added to a case base that already contain instances with the method addInstancesJSON
    @Test
    public void addInstancesJSONCaseBaseNotEmptyTest() throws Exception {
        // add instances to case base
        addInstancesWithCaseBaseService();

        // Count the instances added
        Project p = App.getProject();
        int numberOfInstances = p.getAllInstances().size();

        // check if the instance exists in the case base
        Assertions.assertFalse(isCaseBaseEmpty(), "Case base should contain an instance, but is empty");

        // add an instance
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // check that the instance was added
        Assertions.assertTrue(numberOfInstances + 1 == p.getAllInstances().size(), "Instance was not added");
        logger.debug("results..");
    }

    // Test an instance with fault in input with method addInstancesJSON
    @Test
    public void addInstancesJSONInputError() throws Exception {
        // try to add the instance with error. Expect HTTP error 422 "Unprocessable Entity"
        mockMvc.perform(post(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .param("cases", INSTANCES_WITH_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        // check that the instance was not added
        Assertions.assertTrue(isCaseBaseEmpty(), "Case base should not contain instances, but is not empty");
        logger.debug("results..");
    }

    // Test that the method "addInstanceJSON" is adding an instance when the Case Base is empty
    @Test
    public void addInstanceJSONTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // add an instance
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", INSTANCE)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        // check if instance exists with method: "getAllInstancesInCaseBase"
        Assertions.assertTrue(isInstanceInCaseBase(), "Instance was not found in Case Base");
        logger.debug("results..");
    }

    // Test adding an empty case when the Case Base is empty with method "addInstanceJSON"
    @Test
    public void addEmptyInstanceJSONTest() throws Exception {
        // Confirm the case base is empty
        Project p = App.getProject();
        if (!p.hasCB(CASE_BASE_NAME)) {
            throw new IllegalArgumentException("Case base is not empty"); }

        //  add an empty instance. Expect HTTP error 422 "Unprocessable Entity"
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", EMPTY_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        // check that nothing has been added to the Case Base
        if (!p.hasCB(CASE_BASE_NAME)) {
            throw new IllegalArgumentException("Case base is not empty"); }
        logger.debug("results..");
    }

    // Test that the method "addInstanceJSON" is adding an instance when the Case Base is not empty
    @Test
    public void addInstanceJSONCaseBaseNotEmptyTest() throws Exception {
        // add instances to case base
        addInstancesWithCaseBaseService();

        // Count the instances added
        Project p = App.getProject();
        int numberOfInstances = p.getAllInstances().size();

        // check if instances exists in the case base
        Assertions.assertFalse(isCaseBaseEmpty(), "Case base should contain instances, but is empty");

        // add an instance
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", INSTANCE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // check that the instance was added
        Assertions.assertTrue(numberOfInstances + 1 == p.getAllInstances().size(), "Instance was not added");

        // todo: Check that the instance was added correctly
        logger.debug("results..");
    }

    // Test to add an instance with error in input with method addInstanceJSON
    @Test
    public void addInstanceJSONInputError() throws Exception {
        // try to add the instance with error. Expect HTTP error 422 "Unprocessable Entity"
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", INSTANCE_WITH_ERROR)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        // check that the instance was not added
        Assertions.assertTrue(isCaseBaseEmpty(), "Case base should not contain instances, but was not empty");
        logger.debug("results..");
    }

    // test to get an existing instance with the method getInstance
    @Test
    public void getInstanceTest() throws Exception {
        // add an instance
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", INSTANCE)
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

    // test to get an instance when there is no instance in the Case Base
    @Test
    public void getInstanceWithEmptyCaseBaseTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // try to get an instance in an empty Case Base. Expect error 500, Internal Server Error.
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    // test to delete an instance when there is only one instance in the Case Base
    @Test
    public void deleteInstanceTest() throws Exception {
        // add an instance called "car1"
        mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .param("casedata", INSTANCE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // delete the instance "car1"
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    //test to delete an instance with three instances in the Case Base
    @Test
    public void deleteInstanceWithCasesInCaseBaseTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // add three instances to case base
        addInstancesWithCaseBaseService();

        // delete the instance: "testConcept-testCaseBase2"
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + "testConcept-testCaseBase2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Check that there are two instances left in the case base
        Project p = App.getProject();
        int numberOfInstances = p.getAllInstances().size();
        Assertions.assertTrue(numberOfInstances == 2, "There should be two instances left in Case base, but there are not");

        // todo check that the two cases left are "testConcept-testCaseBase1" and "testConcept-testCaseBase3"

    }

    //test to delete an instance with an empty Case Base
    @Test
    public void deleteInstanceWithEmptyCaseBaseTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // try to delete an instance. Expect error 422 Unprosessable Entity.
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("false"));
    }

    // Delete Instance should use delete method in InstanceService, not remove a collection
    // todo: Rename "deleteInstances" to "deleteAllInstances" to avoid confusion.
    @Test
    public void deleteInstancesTest() throws Exception {
        // add an instance: "car1"
        Assertions.assertTrue(addOneInstance(), "Instance was not added correctly");

        // delete the instance: "car1"
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_CASES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // check if instance "car1" is deleted
        mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).isEmpty()))
                .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_2).isEmpty()))
                .andExpect((jsonPath(JSON_PATH+"caseID").isEmpty()));
    }

    // Delete all cases with 3 cases in the Case Base. Using the method with "delete" at the end of the URI
    // TODO: Delete method "deleteInstances" with URI ending with "delete". Bjørn says its enough with one deleteInstances.
    @Test
    public void deleteAllInstancesTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // add three instances to case base
        addInstancesWithCaseBaseService();

        // delete all the instances
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + PATH_DELETE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Check that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

    }
    
    // Delete 1 case with an empty Case Base
    @Test
    public void deleteInstancesWithEmptyCaseBaseTest() throws Exception {
        // Confirm that the case base is empty
        Assertions.assertTrue(isCaseBaseEmpty(), "The case base should be empty but was not empty");

        // delete all the instances
        // todo: what should be the expected return statement when no instance have been deleted correctly
        mockMvc.perform(delete(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + PATH_DELETE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

    }

    // add an instance called "car1"
    private boolean addOneInstance() {
        boolean flag = false;
        try {
            mockMvc.perform(put(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES + TEST_CASE_ID)
                    .param("casedata", "{" +
                            "\"" + ATT_DOUBLE_1 +"\""+ ":0.7,"+
                            "\"" + ATT_DOUBLE_2 +"\""+ ":0.4"+
                            "}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean isCaseBaseEmpty() {
      Project p = App.getProject();
      if (p.getAllInstances().size() == 0) {
          return true;
      } else {return false;}
    }

    private boolean isInstanceInCaseBase() {
        boolean flag = false;
        try {
            mockMvc.perform(get(PATH_CONCEPT + CONCEPT_NAME + PATH_CASEBASE + CASE_BASE_NAME + PATH_INSTANCES)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_1).value("0.7")))
                    .andExpect((jsonPath(JSON_PATH+ATT_DOUBLE_2).value("0.4")))
                    .andExpect((jsonPath(JSON_PATH+"caseID").value("car1")));
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    private void addInstancesWithCaseBaseService() {
        Concept c = conceptService.getProject().getConceptByID(CONCEPT_NAME);
        // create a set for new instance data
        Set<Map<AttributeDesc,String>> instances = new HashSet<>();

        // data for instance 1
        instances.add(new HashMap<AttributeDesc, String>() {{
            put(c.getAttributeDesc(ATT_DOUBLE_1), "1.0");
            put(c.getAttributeDesc(ATT_DOUBLE_2), "1.0");
            put(c.getAttributeDesc(ATT_DOUBLE_3), "1.0");
        }});

        // data for instance 2
        instances.add(new HashMap<AttributeDesc, String>() {{
            put(c.getAttributeDesc(ATT_DOUBLE_1), "0.5");
            put(c.getAttributeDesc(ATT_DOUBLE_2), "0.5");
            put(c.getAttributeDesc(ATT_DOUBLE_3), "0.5");
        }});

        // data for instance 3
        instances.add(new HashMap<AttributeDesc, String>() {{
            put(c.getAttributeDesc(ATT_DOUBLE_1), "0.1");
            put(c.getAttributeDesc(ATT_DOUBLE_2), "0.1");
            put(c.getAttributeDesc(ATT_DOUBLE_3), "0.1");
        }});

        instanceService.addInstances(c, CASE_BASE_NAME, instances);
    }



    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        logger.info(conceptService);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        ts = new TestSetupInstanceController();
        ts.createTestCaseBase(conceptService, caseBaseService);
    }

    // Alternatively use delete method with mockMvc to delete
    @After
    public void after() {
        ts.deleteTestCaseBase(caseBaseService);
    }
}
