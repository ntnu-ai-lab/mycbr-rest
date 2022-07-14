package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.retrieval.Retrieval;
import no.ntnu.mycbr.core.retrieval.Retrieval.RetrievalCustomer;
import no.ntnu.mycbr.core.similarity.Similarity;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.service.CaseBaseService;
import no.ntnu.mycbr.rest.controller.service.CaseService;
import no.ntnu.mycbr.rest.controller.service.ConceptService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class EphemeralControllerTest implements RetrievalCustomer {

    private final static String PATH_SEPARATOR = File.separator;

    private final Log logger = LogFactory.getLog(getClass());
    private MockMvc mockMvc;

    private final static String TEST_RESOURCES_PATH = PATH_SEPARATOR + "src" + PATH_SEPARATOR + "test" + PATH_SEPARATOR
	    + "resources" + PATH_SEPARATOR;
    private final static String MYCBR_PROJECT_FILE_PATH = TEST_RESOURCES_PATH + "car_mycbr_project.prj";

    private final static String TEST_CONCEPT_ID = "car";
    private final static String TEST_CASEBASE_ID = "main_casebase";
    private final static String TEST_AMAL_FUNC_ID = "equal_weight_gsf";

    private final static String URL_PATH = "/ephemeral" + "/concepts/" + TEST_CONCEPT_ID + "/casebases/"
	    + TEST_CASEBASE_ID + "/amalgamationFunctions/" + TEST_AMAL_FUNC_ID + "/retrievalByCaseIDs?k=-1";

    private final static String TEST_API_POST_BODY_10_CASES = "{ " + "\"queryCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\"" + "], "
	    + "\"ephemeralCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\"" + "]}";

    private final static String TEST_API_POST_BODY_50_CASES = "{ " + "\"queryCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\","
	    + "\"car10\", \"car12\", \"car13\", \"car14\", \"car15\", \"car16\", \"car17\", \"car18\", \"car19\","
	    + "\"car20\", \"car22\", \"car23\", \"car24\", \"car25\", \"car26\", \"car27\", \"car28\", \"car29\","
	    + "\"car30\", \"car32\", \"car33\", \"car34\", \"car35\", \"car36\", \"car37\", \"car38\", \"car39\","
	    + "\"car40\", \"car42\", \"car43\", \"car44\", \"car45\", \"car46\", \"car47\", \"car48\", \"car49\""
	    + "], " + "\"ephemeralCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\","
	    + "\"car10\", \"car12\", \"car13\", \"car14\", \"car15\", \"car16\", \"car17\", \"car18\", \"car19\","
	    + "\"car20\", \"car22\", \"car23\", \"car24\", \"car25\", \"car26\", \"car27\", \"car28\", \"car29\","
	    + "\"car30\", \"car32\", \"car33\", \"car34\", \"car35\", \"car36\", \"car37\", \"car38\", \"car39\","
	    + "\"car40\", \"car42\", \"car43\", \"car44\", \"car45\", \"car46\", \"car47\", \"car48\", \"car49\""
	    + "]}";

    private final static String TEST_API_POST_BODY_100_CASES = "{ " + "\"queryCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\","
	    + "\"car10\", \"car12\", \"car13\", \"car14\", \"car15\", \"car16\", \"car17\", \"car18\", \"car19\","
	    + "\"car20\", \"car22\", \"car23\", \"car24\", \"car25\", \"car26\", \"car27\", \"car28\", \"car29\","
	    + "\"car30\", \"car32\", \"car33\", \"car34\", \"car35\", \"car36\", \"car37\", \"car38\", \"car39\","
	    + "\"car40\", \"car42\", \"car43\", \"car44\", \"car45\", \"car46\", \"car47\", \"car48\", \"car49\","
	    + "\"car50\", \"car52\", \"car53\", \"car54\", \"car55\", \"car56\", \"car57\", \"car58\", \"car59\","
	    + "\"car60\", \"car62\", \"car63\", \"car64\", \"car65\", \"car66\", \"car67\", \"car68\", \"car69\","
	    + "\"car70\", \"car72\", \"car73\", \"car74\", \"car75\", \"car76\", \"car77\", \"car78\", \"car79\","
	    + "\"car80\", \"car82\", \"car83\", \"car84\", \"car85\", \"car86\", \"car87\", \"car88\", \"car89\","
	    + "\"car90\", \"car92\", \"car93\", \"car94\", \"car95\", \"car96\", \"car97\", \"car98\", \"car99\""
	    + "], " + "\"ephemeralCaseIDs\": [ "
	    + "\"car0\", \"car2\", \"car3\", \"car4\", \"car5\", \"car6\", \"car7\", \"car8\", \"car9\","
	    + "\"car10\", \"car12\", \"car13\", \"car14\", \"car15\", \"car16\", \"car17\", \"car18\", \"car19\","
	    + "\"car20\", \"car22\", \"car23\", \"car24\", \"car25\", \"car26\", \"car27\", \"car28\", \"car29\","
	    + "\"car30\", \"car32\", \"car33\", \"car34\", \"car35\", \"car36\", \"car37\", \"car38\", \"car39\","
	    + "\"car40\", \"car42\", \"car43\", \"car44\", \"car45\", \"car46\", \"car47\", \"car48\", \"car49\","
	    + "\"car50\", \"car52\", \"car53\", \"car54\", \"car55\", \"car56\", \"car57\", \"car58\", \"car59\","
	    + "\"car60\", \"car62\", \"car63\", \"car64\", \"car65\", \"car66\", \"car67\", \"car68\", \"car69\","
	    + "\"car70\", \"car72\", \"car73\", \"car74\", \"car75\", \"car76\", \"car77\", \"car78\", \"car79\","
	    + "\"car80\", \"car82\", \"car83\", \"car84\", \"car85\", \"car86\", \"car87\", \"car88\", \"car89\","
	    + "\"car90\", \"car92\", \"car93\", \"car94\", \"car95\", \"car96\", \"car97\", \"car98\", \"car99\""
	    + "]}";

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private CaseBaseService caseBaseService;

    @Autowired
    private CaseService caseService;

    @BeforeClass
    public static void setUp() {
	System.setProperty("MYCBR.PROJECT.FILE", System.getProperty("user.dir") + MYCBR_PROJECT_FILE_PATH);
    }

    @Before
    public void before() throws Exception {
	try {
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

    @Test
    public void retrievalFromEphemeralCaseBaseTest() throws Exception {

	logger.info("The MYCBR.PROJECT.FILE system property's value is : " + System.getProperty("MYCBR.PROJECT.FILE"));

	MockHttpServletRequestBuilder servlet = post(URL_PATH)
		.content(TEST_API_POST_BODY_100_CASES)
		.contentType(MediaType.APPLICATION_JSON);
	try {
	    ResultActions res = mockMvc.perform(servlet);
	    // res.andDo(MockMvcResultHandlers.print());
	    res.andDo(MockMvcResultHandlers.log());
	    res.andExpect(status().isOk());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * This test causes exceptions, at
     * no.ntnu.mycbr.core.model.Concept.setActiveAmalgamFct(Concept.java:817), while
     * setting the user defined amalgamation function from the set of available
     * amalgamation functions. The exception was observed for the variable
     * (threadCount) value to be 100, on macbook pc.
     * 
     * @throws Exception
     */
    @Test
    public void retrievalFromEphemeralCaseBaseTestMultiThread() throws Exception {

	int threadCount = 10;

	final MockHttpServletRequestBuilder servlet = post(URL_PATH)
		.content(TEST_API_POST_BODY_100_CASES)
		.contentType(MediaType.APPLICATION_JSON);

	Runnable runnable = () -> {

	    try {

		ResultActions res = mockMvc.perform(servlet);
		res.andExpect(status().isOk());

		// System.out.println(res.andReturn().getResponse().getContentAsString());

	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	};

	List<Thread> threads = new LinkedList<>();

	// executing the retrieval REST API calls in separate threads for concurrency
	// testing
	for (int i = 0; i < threadCount; i++) {
	    Thread thread = new Thread(runnable);
	    threads.add(thread);
	    thread.start();
	}

	// so that the control waits for completion of all the threads
	for (Thread thread : threads)
	    thread.join();
    }

    @Override
    public void addResults(Retrieval retrieval, List<Pair<Instance, Similarity>> list) {
    }
}
