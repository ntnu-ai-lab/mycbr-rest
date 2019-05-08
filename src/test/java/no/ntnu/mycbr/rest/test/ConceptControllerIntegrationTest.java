package no.ntnu.mycbr.rest.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.ConceptController;
import no.ntnu.mycbr.rest.service.ConceptService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class ConceptControllerIntegrationTest {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private ConceptService conceptService;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void addConceptTest()
            throws Exception {
        //add a concept
        mockMvc.perform(put("/concepts/mytestconcept")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //check if it exists..
        mockMvc.perform(get("/concepts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept").value("mytestconcept"));
        logger.debug("results..");
    }

    @Test
    public void addAttributes()
            throws Exception {
        //add a concept

        mockMvc.perform(put("/concepts/mytestconcept")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        addDoubleAttribute("doubleattribute1",0,1);
        addDoubleAttribute("doubleattribute2",0,2);
        addStringAttribute("stringattribute1");
        addSymbolAttribute("symbolattribute1","[\"symbolvalue1\",\"symbolvalue2\"]");
        //check if it exists..
        ResultActions res = mockMvc.perform(get("/concepts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concept").value("mytestconcept"));
        res = mockMvc.perform(get("/concepts/mytestconcept/attributes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.attributes", Matchers.hasKey("doubleattribute1"))))
                .andExpect((jsonPath("$.attributes", Matchers.hasKey("doubleattribute2"))))
                .andExpect((jsonPath("$.attributes", Matchers.hasKey("stringattribute1"))))
                .andExpect((jsonPath("$.attributes", Matchers.hasKey("symbolattribute1"))));

        logger.debug("results.."+res.toString());
    }
    private void addDoubleAttribute(String name, double min, double max)
    throws  Exception{
        mockMvc.perform(put("/concepts/mytestconcept/attributes/"+name)
                .param("attributeJSON","{" +
                        "\"type\": \"Double\"," +
                        "\"min\":"+Double.toString(min)+"," +
                        "\"max\":"+Double.toString(max)+"," +
                        "\"solution\": \"false\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    private void addStringAttribute(String name)
            throws  Exception{
        mockMvc.perform(put("/concepts/mytestconcept/attributes/"+name)
                .param("attributeJSON","{" +
                        "\"type\": \"String\"," +
                        "\"solution\": \"false\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    private void addSymbolAttribute(String name, String allowedValues)
            throws  Exception{
        mockMvc.perform(put("/concepts/mytestconcept/attributes/"+name)
                .param("attributeJSON","{" +
                        "\"type\": \"Symbol\"," +
                        "\"allowedValues\": "+ allowedValues +","+
                        "\"solution\": \"false\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        conceptService.deleteAllConcepts();
    }

    private byte[] toJson(Object o) throws Exception{
        return this.mapper.writeValueAsString(o).getBytes();
    }
}
