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
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static no.ntnu.mycbr.rest.common.CommonConstants.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
Method "setUp" takes care of System Properties. This is for importing the project file correctly (The alternatives are to alter the App.class or 
manually change System properties as described under)
Configurations can be changed by (intellij): Run -> Edit config. -> RetrievalCont... -> Change VM options to: -DMYCBR.PROJECT.FILE=used_cars_flat.prj
Remember: You can't have the project file opened another place and run the test at the same time.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class RetrievalControllerIntegrationTest implements Retrieval.RetrievalCustomer {
    
    private static final String CASE_ID_CAR_0 = "['car #0']";
    private static final String CASE_ID_CAR_1 = "['car #1']";
    private static final String CASE_ID_CAR_2 = "['car #2']";
    private static final String CASES_FOR_RETRIEVAL_BY_IDS = "[\"car #2\", \"car #1\"]";
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

    // Test similarity value and the order of the result with method "getSimilarInstancesByID". Should be in descending order.
    @Test
    public void getSimilarInstancesByIDTest() throws Exception {
        ResultActions res = mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ID)
                .param("caseID", "car #0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_0).value(1.0)))
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_1).value(0.63)))
                .andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_2).value(0.755)));

        // Check that the ordering is descending
        res.andExpect(content().string("{\"similarCases\":{\"car #0\":1.0,\"car #2\":0.7550000000000001,\"car #1\":0.63}}"));
    }

    // Test similarity value and the order of the result with method "getSimilarInstancesByAttribute". Should be in descending order.
    @Test
    public void getSimilarInstancesByAttributeTest() throws Exception {
        // todo: replace values with manually calculated similarity values

        // Check similarity value
        ResultActions res = mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ATTRIBUTE)
                .param("concept name", "car")
                .param("amalgamation function", "carFunc")
                .param("Symbol attribute name", "manufacturer")
                .param("value", "vw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

            res.andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_0).value(0.27)));
            res.andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_1).value(0.225)));
            res.andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_2).value(0.45)));

        // Check that the ordering is descending
        res.andExpect(content().string("{\"similarCases\":{\"car #2\":0.45,\"car #0\":0.27,\"car #1\":0.225}}"));

    }

    // Test similarity value and the order of the result with method "getSimilarInstancesByAttributeWithContent". Should be in descending order.
    @Test
    public void getSimilarInstancesByAttributeWithContentTest() throws Exception {
        // Todo: Can't get the method to work, both on REST API and the test.
        // Check similarity value
        ResultActions res = mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ATTRIBUTE_WITH_CONTENT)
                .param("conceptID", "car")
                .param("casebaseID", "CaseBase0")
                .param("amalgamation function", "carFunc")
                .param("Symbol attribute name", "manufacturer")
                .param("value", "mercedes")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        //res.andExpect((jsonPath(JSON_PATH + CASE_ID_CAR_0).value(SIM_CAR_0)));
    }

    // Test similarity value and ordering of the result with method "getSimilarInstancesByIDs". Should be in descending order.
    @Test
    public void getSimilarInstancesByIDsTest() throws Exception {
        mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_IDs)
                .param("caseIDs", CASES_FOR_RETRIEVAL_BY_IDS)
                .param("casebaseID", "CaseBase0")
                .param("conceptID", "car")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                // Test similarity value of "car #1"
                .andExpect(jsonPath("$.['car #1'].['car #0']").value(0.63))
                .andExpect(jsonPath("$.['car #1'].['car #1']").value(1))
                .andExpect(jsonPath("$.['car #1'].['car #2']").value(0.525))
                // Test similarity value of "car #2"
                .andExpect(jsonPath("$.['car #2'].['car #0']").value(0.7550000000000001))
                .andExpect(jsonPath("$.['car #2'].['car #1']").value(0.525))
                .andExpect(jsonPath("$.['car #2'].['car #2']").value(1)
                );
    }

    // Test similarity value, content and ordering of the result with method "getSimilarInstancesByIDWithContent". Should be in descending order.
    @Test
    public void getSimilarInstancesByIDWithContentTest() throws Exception {
        ResultActions res = mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_ID_WITH_CONTENT)
                .param("amalgamation function", "carFunc")
                .param("caseID", "car #0")
                .param("casebaseID", "CaseBase0")
                .param("conceptID", "car")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        // Check that the content and that the ordering is descending
        res.andExpect(content().string("[{\"similarity\":\"0.63\",\"caseID\":\"car #1\",\"ccm\":\"5000\",\"manufacturer\":\"mercedes\",\"price\":\"80000.0\"},{\"similarity\":\"0.7550000000000001\",\"caseID\":\"car #2\",\"ccm\":\"3000\",\"manufacturer\":\"vw\",\"price\":\"30000.0\"},{\"similarity\":\"1.0\",\"caseID\":\"car #0\",\"ccm\":\"2000\",\"manufacturer\":\"bmw\",\"price\":\"40000.0\"}]"));
    }

    @Test
    public void getSimilarInstancesByIDsWithinIDsTest() throws Exception {
        mockMvc.perform(get(PATH_CONCEPT + CAR_CONCEPT_NAME + PATH_CASEBASE + CAR_CASE_BASE + PATH_RETRIEVAL_BY_IDS_IN_IDS)
                .param("caseIDs", casesArray)
                .param("filterCaseIDs", casesArray)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    @BeforeClass
    public static void setUp() {
        System.setProperty("MYCBR.PROJECT.FILE", System.getProperty("user.dir") + "\\src\\test\\resources\\cars_for_testing.prj");
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

    @Override
    public void addResults(Retrieval retrieval, List<Pair<Instance, Similarity>> list) {
    }
}