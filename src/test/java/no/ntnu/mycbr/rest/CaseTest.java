package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.rest.Case;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CaseTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCase() {
        String caseID = "dummyID";
        Case case1 = null;
        try {
            case1 = new Case(caseID);
        } catch (Exception e){}
        LinkedHashMap<String, String> content = case1.getCase();
        assertNotNull(content, "Case content for " + caseID + "is null");
        //assert
    }

    @Test
    void caseConstructionTest() {
        String caseID = "dummyID";
        Case case1 = null;
        try {
            case1 = new Case(caseID);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException, "Exception is not of type NullPointer, but of type "+e);
        }
    }
}