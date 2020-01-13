package no.ntnu.mycbr.rest.controller;

import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import no.ntnu.mycbr.rest.common.ApiResponseAnnotations.ApiResponsesDefault;
import no.ntnu.mycbr.rest.controller.service.AttributeService;


/**
 * This controller class is responsible to receiving all REST requests pertaining to attributes of 
 * a given concept, including attribute level similarity functions.
 * This class only delegates the calls to respective service classes to handle the call requests.
 * @author Amar Jaiswal
 * @since 08 Jan 2020
 */
@RestController
public class AttributeController {
    
    private final Log logger = LogFactory.getLog(getClass());
    private static final String STRING = "String";
    
    //get an attribute by ID
    @ApiOperation(value = GET_ATTRIBUTE_BY_ID, nickname = GET_ATTRIBUTE_BY_ID)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Object> getAttributeByID(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	return new AttributeService().getAttributeByID(conceptID, attributeID);
    }
    
   /* // get attribute description (details)
    @ApiOperation(value = GET_ATTRIBUTE_DESCTIPTION, nickname = GET_ATTRIBUTE_DESCTIPTION)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_DESC, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Object> getAttributeDiscription(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	AttributeService service = new AttributeService(conceptID, attributeID);
	
	return service.getAttributeDiscription();
    } */

    //get All attributes
    @ApiOperation(value = GET_ALL_ATTRIBUTES, nickname = GET_ALL_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTRS, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, String> getAllAttributes(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	AttributeService service = new AttributeService(conceptID);
	
	return service.getAllAttributes();
    }
    
    //add one attribute
    @ApiOperation(value = ADD_ATTRIBUTE_BY_ID, nickname = ADD_ATTRIBUTE_BY_ID)
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean addAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID,
	    @RequestParam(value="attributeJSON", defaultValue = "{}") String attributeJSON) {
	
	return new AttributeService().addAttribute(conceptID, attributeID, attributeJSON);
    }
    
    //delete one attribute
    @ApiOperation(value = DELETE_ATTRIBUTE_BY_ID, nickname = DELETE_ATTRIBUTE_BY_ID)
    @RequestMapping(method = RequestMethod.DELETE, value =  PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	return new AttributeService().deleteAttribute(conceptID, attributeID);
    }
    
    //delete all  attributes
    @ApiOperation(value = DELETE_ALL_ATTRIBUTES, nickname = DELETE_ALL_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_ATTRS, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAllAttributes(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	return new AttributeService().deleteAllAttribute(conceptID);
    }
    
    //Get all similarity function for attribute
    @ApiOperation(value = GET_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS, nickname = GET_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_SIM_FUNCTIONS, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public Map<String, Object> getAllSimilarityFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	AttributeService service = new AttributeService(conceptID, attributeID);
	
	return service.getAllSimilarityFunctions();
    }
    
    //Delete all similarity function for attribute
    @ApiOperation(value = DELETE_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS, nickname = DELETE_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_ATTR_SIM_FUNCTIONS, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAllSimilarityFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	AttributeService service = new AttributeService(conceptID, attributeID);

	return service.deleteAllSimilarityFunctions();
    }
    
    // add an attribute similarity function
    @ApiOperation(value = ADD_ATTRIBUTE_SIMILARITY_FUNCTION, nickname = ADD_ATTRIBUTE_SIMILARITY_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_ATTR_SIM_FUNCTION_ID, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean addSimilarityFunction(
	    @PathVariable(value=CONCEPT) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID,
	    @PathVariable(value=SIM_FUNCTION_ID) String similarityFunctionID,
	    @RequestParam(value="parameter", defaultValue="1.0") Double parameter) {
	
	return new AttributeService().addDoubleSimilarityFunction( conceptID, attributeID, similarityFunctionID, parameter);
    }
}
