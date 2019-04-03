package no.ntnu.mycbr.rest.test;

import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.rest.controller.ConceptController;
import no.ntnu.mycbr.rest.service.ConceptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(ConceptController.class)
@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
public class ConceptControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConceptService conceptService;

    @Test
    public void addConceptTest()
        throws Exception {
        // Concept concept = conceptService.createTopConcept("mytestconcept");
        mvc.perform(put("concepts/mytestconcept")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
