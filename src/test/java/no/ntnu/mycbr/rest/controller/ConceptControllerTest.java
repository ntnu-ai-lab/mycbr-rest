package no.ntnu.mycbr.rest.controller;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ConceptController.class)
public class ConceptControllerTest extends TestCase {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConceptController conceptController;
    
    public void testGetAmalgamationFunctions() {
    }

    public void testDeleteAmalgamationFunctions() {
    }

    public void testAddAmalgamationFunctions() {
    }

    public void testDeleteAmalgamationFunction() {
    }

    public void testGetConcepts() {
    }

    public void testDeleteConcepts() {
    }

    public void testDeleteConcept() {
    }

    public void testAddConcept() {
    }

    public void testGetAttributes() {
    }

    public void testDeleteAttributes() {
    }

    public void testAddAttribute() {
    }

    public void testDeleteAttribute() {
    }

    public void testGetSimilarityFunction() {
    }

    public void testDeleteSimilarityFunctions() {
    }

    public void testAddSimilarityFunction() {
    }

    public void testCreateTopConcept() {
    }

    public void testGetValueRange() {
    }
}