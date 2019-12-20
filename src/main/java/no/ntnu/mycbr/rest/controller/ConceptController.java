package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.*;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.*;
import no.ntnu.mycbr.rest.service.ConceptService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static no.ntnu.mycbr.rest.common.ApiResponseAnnotations.*;
import static no.ntnu.mycbr.rest.common.ApiPathConstants.*;
import static no.ntnu.mycbr.rest.common.ApiOperationConstants.*;

@RestController
public class ConceptController {

    private static final String MAIN_CASEBASE = "main_casebase";
    private static final String STRING = "String";
    private final Log logger = LogFactory.getLog(getClass());
    private String file_path =  System.getProperty("java.io.tmpdir");
    @Autowired
    private ConceptService conceptService;

    //get all amalgamation functions
    @ApiOperation(value = GET_ALL_AMALGAMATION_FUNCTIONS, nickname = GET_ALL_AMALGAMATION_FUNCTIONS)
    //@RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS + "/{concept}/amalgamationFunctions", produces = APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
    @ApiResponsesForAmalgamationFunctions
    public List<String> getAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	AmalgamationFunctions amal = new AmalgamationFunctions(conceptID);
	return amal.getAmalgamationFunctionIDs();
    }
    
    //delete all amalgamation functions
    @ApiOperation(value = DELETE_ALL_AMALGAMATION_FUNCTIONS, nickname = DELETE_ALL_AMALGAMATION_FUNCTIONS)
    @RequestMapping(method = RequestMethod.DELETE, path= PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	Concept thisconcept = App.getProject().getAllSubConcepts().get(conceptID);
	List<AmalgamationFct> list = thisconcept.getAvailableAmalgamFcts();
	for(AmalgamationFct fct : list){
	    thisconcept.deleteAmalgamFct(fct);
	}
	return true;
    }

    //add one amalgationfunction
    // amalgamationfunctionType needs to be a string matching exactly the name of the enum. https://stackoverflow.com/questions/604424/lookup-java-enum-by-string-value
    // MINIMUM, MAXIMUM, WEIGHTED_SUM, EUCLIDEAN, NEURAL_NETWORK_SOLUTION_DIRECTLY,SIM_DEF;
    @ApiOperation(value = ADD_AMALGAMATION_FUNCTION, nickname = ADD_AMALGAMATION_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, path=PATH_CONCEPT_AMAL_FUNCTION_ID, produces = APPLICATION_JSON)
    @ApiResponsesForAmalgamationFunctions
    public boolean addAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value=AMAL_FUNCTION_TYPE) String amalgamationFunctionType) {
	
	logger.info("in add amalgamationfunction");
	Concept concept = App.getProject().getSubConcepts().get(conceptID);
	AmalgamationConfig config = AmalgamationConfig.valueOf(amalgamationFunctionType);
	AmalgamationFct fct = concept.addAmalgamationFct(config,amalgamationFunctionID, false);
	concept.setActiveAmalgamFct(fct);

	return true;
    }

    //save file
    private void saveUploadedFiles(HashMap<MultipartFile,String> filesAndNames) throws IOException {

	for (MultipartFile file : filesAndNames.keySet()) {

	    if (file.isEmpty()) {
		continue; //next pls
	    }

	    byte[] bytes = file.getBytes();
	    Path path = Paths.get(filesAndNames.get(file));
	    Files.write(path, bytes);

	}

    }
    //add one amalgationfunction
    // amalgamationfunctionType needs to be a string matching exactly the name of the enum. https://stackoverflow.com/questions/604424/lookup-java-enum-by-string-value
    // MINIMUM, MAXIMUM, WEIGHTED_SUM, EUCLIDEAN, NEURAL_NETWORK_SOLUTION_DIRECTLY,SIM_DEF;
    /*@ApiOperation(value = "addNeuralAmalgamationFunction", nickname = "addANeuralmalgamationFunction")
    @RequestMapping(method = RequestMethod.PUT, path="/concepts/{conceptID}/neuralAmalgamationFunctions/{amalgamationFunctionID}", produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AmalgamationFunctions.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> addNeuralAmalgamationFunctions(@PathVariable(value=CONCEPT_ID) String conceptID,
                                                            @PathVariable(value=PATH_AMALGAMATION_FUNCTION_ID) String amalgamationFunctionID,
                                                            @RequestParam(value="files") MultipartFile[] uploadfiles) {
        //First collect the files, save them and check them
        logger.info("in add neuralamalgamationfunction");

        List<String> uploadedFileNames = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.toList());

        if(uploadedFileNames.size() != 2){
            return new ResponseEntity<>("please upload both .h5 and .json file",HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(uploadedFileNames)) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        String firstfile = uploadedFileNames.get(0);
        String baseFileName = firstfile.substring(0,firstfile.lastIndexOf("."));
        baseFileName = file_path+baseFileName;

        try {

            saveUploadedFiles(Arrays.asList(uploadfiles));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("setting NeuralRetrievalModelFilePath to \""+baseFileName+"\"");
        System.setProperty("NeuralRetrievalModelFilePath",baseFileName);
        //Then create the function
        Concept concept = App.getProject().getSubConcepts().get(conceptID);
        AmalgamationConfig config = AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY; //NICE
        AmalgamationFct fct = concept.addAmalgamationFct(config,amalgamationFunctionID, false);
        concept.setActiveAmalgamFct(fct);
        return new ResponseEntity<>("Files uploaded and neural amalgamation function created",HttpStatus.OK);
    }*/


    // 3.1.1 Single file upload

    // If not @RestController, uncomment this
    //@ResponseBody
    @ApiOperation(value = ADD_NEURAL_AMALGAMATION_FUNCTION, nickname = ADD_NEURAL_AMALGAMATION_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, 
    	path=PATH_CONCEPT_ID + "/neuralAmalgamationFunctions/{amalgamationFunctionID}", 
    	produces = APPLICATION_JSON)
    @ApiResponsesForResponseEntity
    public ResponseEntity<?> addNeuralAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value="type") String type,
	    @RequestParam(value="h5file") MultipartFile h5file,
	    @RequestParam(value="jsonfile") MultipartFile jsonfile) {

	logger.info("adding new Amalgamation Function");
	if (h5file.isEmpty() || jsonfile.isEmpty()) {
	    return new ResponseEntity("please select a file!", HttpStatus.OK);
	}
	String firstfile = jsonfile.getOriginalFilename();
	String baseFileName = firstfile.substring(firstfile.lastIndexOf("/")+1,firstfile.lastIndexOf("."));
	if(baseFileName.contentEquals("/"))
	    baseFileName = file_path+baseFileName;
	else
	    baseFileName = file_path+"/"+baseFileName;
	try {
	    HashMap<MultipartFile,String> map = new HashMap<>();
	    map.put(h5file,baseFileName+".h5");
	    map.put(jsonfile,baseFileName+".json");
	    saveUploadedFiles(map);

	} catch (IOException e) {
	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	System.setProperty("NeuralRetrievalModelFilePath",baseFileName);
	//Then create the function
	Concept concept = App.getProject().getSubConcepts().get(conceptID);
	if(type.contains("direct")) {
	    AmalgamationConfig config = AmalgamationConfig.NEURAL_NETWORK_SOLUTION_DIRECTLY; //NICE
	    AmalgamationFct fct = concept.addAmalgamationFct(config, amalgamationFunctionID, false);
	    concept.setActiveAmalgamFct(fct);
	}else if(type.contains("gabel")){
	    AmalgamationConfig config = AmalgamationConfig.NEURAL_NETWORK_SOLUTION_GABEL; //NICE
	    AmalgamationFct fct = concept.addAmalgamationFct(config, amalgamationFunctionID, false);
	    concept.setActiveAmalgamFct(fct);
	}
	return new ResponseEntity("Successfully uploaded", HttpStatus.OK);

    }

    //delete one amalgationfunction
    @ApiOperation(value = DELETE_AMALGAMATION_FUNCTION, nickname = DELETE_AMALGAMATION_FUNCTION)
    //@RequestMapping(method = RequestMethod.DELETE, 
    //	path=PATH_CONCEPT_AMAL_FUNCTIONS+"/{amalgamationFunction}", 
    //	produces = APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.DELETE, path=PATH_CONCEPT_AMAL_FUNCTION_ID, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAmalgamationFunction(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION) String amalgamationFunction) {
	
	Concept thisconcept = App.getProject().getAllSubConcepts().get(conceptID);
	List<AmalgamationFct> list = thisconcept.getAvailableAmalgamFcts();
	for(AmalgamationFct fct : list){
	    if(fct.getName().contentEquals(amalgamationFunction)){
		thisconcept.deleteAmalgamFct(fct);
		return true;
	    }
	}
	return false;
    }


    //Get all concepts
    @ApiOperation(value = GET_All_CONCEPTS, nickname = GET_All_CONCEPTS)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS, produces = APPLICATION_JSON)
    @ApiResponsesForConceptName
    public ConceptName getConcepts() {
	
	return new ConceptName();
    }

    //Delete all concepts
    @ApiOperation(value = DELETE_ALL_CONCEPTS, nickname = DELETE_ALL_CONCEPTS)
    @RequestMapping(method = RequestMethod.DELETE, path=PATH_CONCEPTS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteConcepts() {
	
	return conceptService.deleteAllConcepts();
    }



    //Delete one concept
    @ApiOperation(value= DELETE_CONCEPT_BY_ID, nickname= DELETE_CONCEPT_BY_ID)
    @RequestMapping(method=RequestMethod.DELETE, value = PATH_CONCEPT_ID)
    @ApiResponsesForValueRange
    public boolean deleteConcept(
	    @PathVariable(value=CONCEPT_ID) String conceptID){
	
	Project p = App.getProject();
	logger.info("deleting concept with id:"+conceptID);
	Concept c = p.getSubConcepts().get(conceptID);
	//TODO: this should be filtered by concept...
	for(String cb : p.getCaseBases().keySet())
	    p.deleteCaseBase(cb);
	c.getSuperConcept().removeSubConcept(conceptID);
	p.save();
	return true;
    }

    //add one concept
    @ApiOperation(value=ADD_CONCEPT_ID, nickname=ADD_CONCEPT_ID)
    @RequestMapping(method=RequestMethod.PUT, value = PATH_CONCEPT_ID)
    @ApiResponsesForValueRange
    public boolean addConcept(
	    @PathVariable(value=CONCEPT_ID) String conceptID){
	
	return null != conceptService.addConcept(conceptID);
    }

    //get all  attributes
    @ApiOperation(value = GET_ATTRIBUTE_BY_ID, nickname = GET_ATTRIBUTE_BY_ID)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesForApiResponse
    public HashMap<String, Object> getAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	Project p = App.getProject();
	Concept c = p.getSubConcepts().get(conceptID);
	HashMap<String, AttributeDesc> allAttributeDescs = c.getAllAttributeDescs();
	if(!allAttributeDescs.containsKey(attributeID))
	    return null;
	return allAttributeDescs.get(attributeID).getRepresentation();
    }


    //get all  attributes
    @ApiOperation(value = GET_ALL_ATTRIBUTES, nickname = GET_ALL_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTRS, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesForApiResponse
    public Attribute getAttributes(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	return new Attribute(conceptID);
    }

    //delete all  attributes
    @ApiOperation(value = DELETE_ALL_ATTRIBUTES, nickname = DELETE_ALL_ATTRIBUTES)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_ATTRS, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAttributes(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	Project p = App.getProject();
	Concept c = p.getSubConcepts().get(conceptID);
	for(String attributeDescName : c.getAllAttributeDescs().keySet()) {
	    c.removeAttributeDesc(attributeDescName);
	}
	return true;
    }
    

    //add one attribute
    @ApiOperation(value = ADD_ATTRIBUTE_BY_ID, nickname = ADD_ATTRIBUTE_BY_ID)
    //@RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_ATTRS + "/{attributeName}", headers=ACCEPT_APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesForValueRange
    public boolean addAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID,
	    @RequestParam(value="attributeJSON", defaultValue = "{}") String attributeJSON) {
	
	Project p = App.getProject();
	Concept c = p.getSubConcepts().get(conceptID);
	JSONParser parser = new JSONParser();
	JSONObject json = null;
	try {
	    json = (JSONObject) parser.parse(attributeJSON);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	String type = (String) json.get("type");
	String solution = (String) json.get("solution");
	try {
	    if (type.contains(STRING)) {
		//This attribute registers with the concept through callback!
		conceptService.addStringAttribute(c,attributeID,solution.contentEquals("True"));
	    } else if(type.contains("Double")){
		if(json.containsKey("min") && json.containsKey("max")) {
		    double min = (Double)json.get("min");
		    double max = (Double)json.get("max");
		    //This attribute registers with the concept through callback!
		    AttributeDesc attributeDesc = conceptService.addDoubleAttribute(c, attributeID, min, max,solution.contentEquals("True"));
		}else
		    return false;

	    } else if(type.contains("Symbol")){
		if(json.containsKey("allowedValues")) {
		    //This attribute registers with the concept through callback!
		    JSONArray arr = (JSONArray) json.get("allowedValues");
		    Set<String> allowedValues = new HashSet<String>();
		    for(Object o : arr){
			allowedValues.add((String)o);
		    }
		    SymbolDesc attributeDesc = new SymbolDesc(c, attributeID, allowedValues);
		    if(solution.contentEquals("True"))
			attributeDesc.setIsSolution(true);
		}else
		    return false;

	    }
	}catch (Exception e){
	    logger.error("got an exception: ",e);
	}
	p.save();
	return true;
    }

    //delete one attribute
    @ApiOperation(value = DELETE_ATTRIBUTE_BY_ID, nickname = DELETE_ATTRIBUTE_BY_ID)
    //@RequestMapping(method = RequestMethod.DELETE, value =  PATH_CONCEPT_ATTRS + "/{attributeName}", 
    //	headers=ACCEPT_APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.DELETE, value =  PATH_CONCEPT_ATTR_ID, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAttribute(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID,
	    @RequestParam(value=ATTR_TYPE, defaultValue = STRING) String attributeType) {
	
	Project p = App.getProject();
	Concept c = p.getSubConcepts().get(conceptID);

	try {
	    c.removeAttributeDesc(attributeID);
	}catch (Exception e){
	    logger.error("got an exception: ",e);
	    return false;
	}
	p.save();
	return true;
    }

    //Get all similarity function for attribute
    @ApiOperation(value = GET_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS, nickname = GET_ALL_ATTRIBUTE_SIMILARITY_FUNCTIONS)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_SIM_FUNCTIONS, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public HashMap<String, Object> getSimilarityFunction(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	Project p = App.getProject();
	Concept concept = p.getConceptByID(conceptID);
	/*Collection<AttributeDesc> attributeDescs = concept.getAllAttributeDescs().values();
        for(AttributeDesc attributeDesc : attributeDescs){
            concept.getActiveAmalgamFct().setActiveFct(attributeDesc,null);
        }*/
	AttributeDesc attributeDesc = concept.getAttributeDesc(attributeID);
	Object o = concept.getActiveAmalgamFct().getActiveFct(attributeDesc);
	if(o instanceof  SimFct){
	    return  ((SimFct)o).getRepresentation();
	}else
	    return null;
    }

    //Delete all similarity function for attribu5te
    @ApiOperation(value = DELETE_ATTRIBUTE_SIMILARITY_FUNCTION, nickname = DELETE_ATTRIBUTE_SIMILARITY_FUNCTION)
    @RequestMapping(method = RequestMethod.DELETE, value = PATH_CONCEPT_ATTR_SIM_FUNCTIONS, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteSimilarityFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {
	
	Project p = App.getProject();
	Concept concept = p.getConceptByID(conceptID);
	Collection<AttributeDesc> attributeDescs = concept.getAllAttributeDescs().values();
	AttributeDesc attributeDesc = concept.getAttributeDesc(attributeID);
	concept.getActiveAmalgamFct().setActiveFct(attributeDesc,null);

	return true;
    }

    //Add one similarity function
    @ApiOperation(value = ADD_ATTRIBUTE_SIMILARITY_FUNCTION, nickname = ADD_ATTRIBUTE_SIMILARITY_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, value = PATH_CONCEPT_ATTR_SIM_FUNCTION_ID, 
    	headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesForValueRange
    public boolean addSimilarityFunction(
	    @PathVariable(value=CONCEPT) String concept,
	    @PathVariable(value=ATTR_ID) String attributeId,
	    @PathVariable(value=SIM_FUNCTION_ID) String similarityFunctionID,
	    @RequestParam(value=CASEBASE_ID, defaultValue=MAIN_CASEBASE) String casebaseID,
	    @RequestParam(value="parameter", defaultValue="1.0") Double parameter) {
	
	Project p = App.getProject();
	if(!p.getCaseBases().containsKey(casebaseID)){
	    return false;
	}
	//p.createTopConcept("heh");
	Concept c = (Concept)p.getSubConcepts().get(concept);
	return conceptService.addDoubleSimilarityFunction( c, attributeId, similarityFunctionID, parameter);

    }


    //helper methods.
    /*
     to support recursive concepts:
     @RequestMapping("/{id}/**")
public void foo(@PathVariable("id") int id, HttpServletRequest request) {
   String restOfTheUrl = (String) request.getAttribute(
       HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
   ...
}
     */


    //??
    @ApiOperation(value = GET_ATTRIBUTE_VALUE_RANGE, nickname = GET_ATTRIBUTE_VALUE_RANGE)
    @RequestMapping(method = RequestMethod.GET, value = PATH_CONCEPT_ATTR_VALUE_RANGE, headers=ACCEPT_APPLICATION_JSON)
    @ApiResponsesForValueRange
    public ValueRange getValueRange(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=ATTR_ID) String attributeID) {

	return new ValueRange(conceptID, attributeID);
    }

}
