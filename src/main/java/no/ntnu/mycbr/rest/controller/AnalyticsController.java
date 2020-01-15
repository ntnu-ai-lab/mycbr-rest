package no.ntnu.mycbr.rest.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.controller.service.AnalyticsService;

import static no.ntnu.mycbr.rest.common.IApiOperationConstants.*;
import static no.ntnu.mycbr.rest.common.IApiPathConstants.*;

/**
 * This controller class is responsible to serving all REST requests related to analytics on a myCBR system.
 * @author Kerstin Bach
 * @updated Amar Jaiswal
 * @since Jan 13, 2020
 */
@RestController
public class AnalyticsController {

    private static final String CASE_ID_1 = "caseID_1";
    private static final String CASE_ID_2 = "caseID_2";
    
    @ApiOperation(value = "compareTwoCases for (weighted sum)", nickname = "compareTwoCases")
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID + "/compareTwoCases", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Double> compareTwoCases(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID_1) String caseID1,
            @RequestParam(value=CASE_ID_2) String caseID2) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.compareTwoCases(caseID1, caseID2);
    }
    
    @ApiOperation(value = "computeLocalSimilarityOfTwoCases for (weighted sum)", nickname = "computeLocalSimilarityOfTwoCases")
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID + "/computeLocalSimilarityOfTwoCases", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Double> computeLocalSimilarityOfTwoCases(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID_1) String caseID1,
            @RequestParam(value=CASE_ID_2) String caseID2) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.computeLocalSimilarityOfTwoCases(caseID1, caseID2);
    }

    @ApiOperation(value = "computeGlobalSimilarityOfTwoCases for (weighted sum)", nickname = "computeGlobalSimilarityOfTwoCases")
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID + "/computeGlobalSimilarityOfTwoCases", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Double> computeGlobalSimilarityOfTwoCases(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID_1) String caseID1,
            @RequestParam(value=CASE_ID_2) String caseID2) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.computeGlobalSimilarityOfTwoCases(caseID1, caseID2);
    }
    
    
    @ApiOperation(value = "computeLocalSimilarityWithAllCases for (weighted sum)", nickname = "computeLocalSimilarityWithAllCases")
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_CASEBASE_AMAL_FUNCTION_ID + "/computeLocalSimilarityWithAllCases", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, LinkedHashMap<String, Double>> computeLocalSimilarityWithAllCases(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=CASEBASE_ID) String casebaseID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID) String caseID) {

        AnalyticsService service = new AnalyticsService(conceptID, casebaseID, amalgamationFunctionID);
        return service.computeLocalSimilarityWithAllCases(caseID);
    }
    
    @ApiOperation(value = "computeGlobalSimilarityWithAllCases for (weighted sum)", nickname = "computeGlobalSimilarityWithAllCases")
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_CASEBASE_AMAL_FUNCTION_ID + "/computeGlobalSimilarityWithAllCases", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, LinkedHashMap<String, Double>> computeGlobalSimilarityWithAllCases(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=CASEBASE_ID) String casebaseID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID) String caseID) {

        AnalyticsService service = new AnalyticsService(conceptID, casebaseID, amalgamationFunctionID);
        return service.computeGlobalSimilarityWithAllCases(caseID);
    }
    
    @ApiOperation(value = "compares 2 instances including weights (weighted sum)", nickname = DETAILED_CASE_COMPARISION)
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID + "/DetailedCaseComparison", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<Map<String, Double>> DetailedCaseComparison(
            @PathVariable(value=CONCEPT_ID) String conceptID,
            @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID_1) String caseID1,
            @RequestParam(value=CASE_ID_2) String caseID2) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.getCaseComparison(caseID1, caseID2);
    }

    @ApiOperation(value = "compares 2 instances and returns the local sim for each attribute", nickname = LOCAL_SIM_COMPARISION)
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID +"/LocalSimComparison", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody List<Map<String, Double>> LocalSimComparison(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
            @RequestParam(value=CASE_ID_1) String caseAID,
            @RequestParam(value=CASE_ID_2) String caseBID) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.getLocalSimComparison(caseAID, caseBID);
    }

    @ApiOperation(value = "Returns the weights for each attribute specified in the global similarity measure", nickname = GLOBAL_WEIGHTS)
    @RequestMapping(method = RequestMethod.GET, path= PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID +"/GlobalWeights", produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public @ResponseBody Map<String, Double> GlobalWeights(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID) {

        AnalyticsService service = new AnalyticsService(conceptID, amalgamationFunctionID);
        return service.getGlobalWeights();
    }
}
