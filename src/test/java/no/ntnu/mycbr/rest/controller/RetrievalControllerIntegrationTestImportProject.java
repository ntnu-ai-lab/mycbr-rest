package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.retrieval.Retrieval;
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.service.CaseBaseService;
import no.ntnu.mycbr.rest.service.ConceptService;
import no.ntnu.mycbr.rest.service.InstanceService;
import no.ntnu.mycbr.util.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.List;

import static no.ntnu.mycbr.rest.common.CommonConstants.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
Configurations are changed to have the ".prj" file run in the "app.class".
Changed by (intellij): Run -> Edit config. -> Retrievalcont... -> Change VM options to: -DMYCBR.PROJECT.FILE=used_cars_flat.prj

 */

// config that works: ( without the spaces after "\" ) -DMYCBR.PROJECT.FILE=C:\ Users\ visitor\ Documents\ myCBR\ mycbr-rest\ src\test\ resources\ used_cars_flat.prj
// todo: working dir: $MODULE_WORKING_DIR$

// todo: Find relative path of used_cars_flat.prj FROM $MODULE_W...$. Change BOTH VM options AND Working directory
// todo: Path URI: C:\Users\visitor\Documents\myCBR\mycbr-rest\src\test\resources

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(App.class)
@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class RetrievalControllerIntegrationTestImportProject implements Retrieval.RetrievalCustomer {

    /*
    Q: Does import need to be mocked?
    Q: Retrieval need engine and method. Does that need to be mocked?
    */
    private final static String PATH_SEPARATOR = File.separator;
    private final static String TEST_RESOURCES_PATH = PATH_SEPARATOR + "src" + PATH_SEPARATOR + "test" + PATH_SEPARATOR
            + "resources" + PATH_SEPARATOR;
    private final static String MYCBR_PROJECT_FILE_PATH = TEST_RESOURCES_PATH + "cars_for_testing.prj";

    private static final String CASE_ID_CAR_0 = "['car #0']";
    private static final String CASE_ID_CAR_1 = "['car #1']";
    private static final String CASE_ID_CAR_2 = "['car #2']";
    private String casesArray = "[\"car #0\", \"car #1\", \"car #2\"]";
    private MockMvc mockMvc;
    private final Log logger = LogFactory.getLog(getClass());


    @Autowired
    WebApplicationContext wac;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private CaseBaseService caseBaseService;

    @Autowired
    private InstanceService instanceService;

    @BeforeClass
    public static void setUp() {
        System.setProperty("MYCBR.PROJECT.FILE", System.getProperty("user.dir") + MYCBR_PROJECT_FILE_PATH);
    }

    @Before
    public void before() throws Exception {
        try {
            // todo change uri to file located under "resource folder"
            MockitoAnnotations.initMocks(this);
            logger.info(conceptService);
            this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() throws Exception {
    }

    // Test similarity value and the order of the result. Should be in descending order.
    @Test
    public void retrievalByIDTest() throws Exception {
        // Check similarity value
        ResultActions res = mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ID)
                .param("caseID", "car #0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_0).value(1.0)))
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_1).value(0.63)))
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_2).value(0.7550000000000001)));

        // Check that the ordering is descending
        // res.andExpect(content().string("{\"similarCases\":{\"car #0\":1.0,\"car #2\":0.7550000000000001,\"car #1\":0.63}}"));
    }

    @Test
    public void getSimilarInstancesByAttributeTest() throws Exception {
        mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ATTRIBUTE)
                .param("concept name", "car")
                .param("amalgamation function", "carFunc")
                .param("Symbol attribute name", "manufacturer")
                .param("value", "vw")
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isOk())
                // todo: insert manually calculated instances
                //.andExpect()
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrievalByIDsInIDsTest() throws Exception {
        mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_IDS_IN_IDS)
                .param("caseIDs", casesArray)
                .param("filterCaseIDs", casesArray)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    @Override
    public void addResults(Retrieval retrieval, List<Pair<Instance, Similarity>> list) {
    }
}