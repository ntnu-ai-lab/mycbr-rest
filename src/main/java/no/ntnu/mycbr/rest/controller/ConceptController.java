package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.*;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.controller.service.ConceptService;
import no.ntnu.mycbr.rest.controller.service.ProjectAccessService;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
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

    private final Log logger = LogFactory.getLog(getClass());
    private String file_path =  System.getProperty("java.io.tmpdir");
    @Autowired
    private ConceptService conceptService;
    @Autowired
    private ProjectAccessService projectAccessService;

    //get all amalgamation functions
    @Operation(summary = GET_ALL_AMALGAMATION_FUNCTIONS, operationId = GET_ALL_AMALGAMATION_FUNCTIONS)
    //@RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS + "/{concept}/amalgamationFunctions", produces = APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public List<String> getAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	List<String> amalgamationFunctionIDs = new LinkedList<>();
	
	Concept concept = projectAccessService.getProject().getConceptByID(conceptID);
	
	List<AmalgamationFct> amalgamationFunctions = concept.getAvailableAmalgamFcts();
        for (AmalgamationFct amalgamationFunction : amalgamationFunctions) {
            amalgamationFunctionIDs.add(amalgamationFunction.getName());
        }
        
	return amalgamationFunctionIDs;
    }
    
    //delete all amalgamation functions
    @Operation(summary = DELETE_ALL_AMALGAMATION_FUNCTIONS, operationId = DELETE_ALL_AMALGAMATION_FUNCTIONS)
    @RequestMapping(method = RequestMethod.DELETE, path= PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	Concept thisconcept = projectAccessService.getProject().getAllSubConcepts().get(conceptID);
	List<AmalgamationFct> list = thisconcept.getAvailableAmalgamFcts();
	for(AmalgamationFct fct : list){
	    thisconcept.deleteAmalgamFct(fct);
	}
	return true;
    }

    // copy an existing amalgationfunction
    // amalgamationfunctionType needs to be a string matching exactly the name of the enum. https://stackoverflow.com/questions/604424/lookup-java-enum-by-string-value
    // MINIMUM, MAXIMUM, WEIGHTED_SUM, EUCLIDEAN, NEURAL_NETWORK_SOLUTION_DIRECTLY,SIM_DEF;
    @Operation(summary = COPY_AMALGAMATION_FUNCTION, operationId = COPY_AMALGAMATION_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, path=PATH_CONCEPT_AMAL_FUNCTION_ID, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean addAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @RequestParam(value= AMAL_FUNCTION_ID) String amalgamationFunctionID,
	    @RequestParam(value=AMAL_FUNCTION_TYPE) String amalgamationFunctionType) {
	
	Concept concept = projectAccessService.getProject().getSubConcepts().get(conceptID);
    conceptService.addAmalgamationFunction(concept, amalgamationFunctionID, amalgamationFunctionType);

	return true;
    }




	//add one amalgationfunction
	// amalgamationfunctionType needs to be a string matching exactly the name of the enum. https://stackoverflow.com/questions/604424/lookup-java-enum-by-string-value
	// MINIMUM, MAXIMUM, WEIGHTED_SUM, EUCLIDEAN, NEURAL_NETWORK_SOLUTION_DIRECTLY,SIM_DEF;
	@Operation(summary = ADD_AMALGAMATION_FUNCTION, operationId = ADD_AMALGAMATION_FUNCTION)
	@RequestMapping(method = RequestMethod.PUT, path=PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
	@ApiResponsesDefault
	public boolean addAmalgamationFunctions(
			@PathVariable(value=CONCEPT_ID) String conceptID,
			@RequestParam(value=AMAL_FUNCTION_ID) String amalgamationFunctionID,
			@RequestParam(value=AMAL_FUNCTION_TYPE) String amalgamationFunctionType,
			@RequestParam(value="attributeWeightsJSON", defaultValue = "{}") String attributeWeightsJSON)
	{
		Concept concept = projectAccessService.getProject().getSubConcepts().get(conceptID);
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {

			json = (JSONObject) parser.parse(attributeWeightsJSON);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AmalgamationConfig config = AmalgamationConfig.valueOf(amalgamationFunctionType);
		AmalgamationFct fct = concept.addAmalgamationFct(config,amalgamationFunctionID, false);
		concept.setActiveAmalgamFct(fct);

		HashMap<String, AttributeDesc> attributeDescMap = concept.getAllAttributeDescs();
		String att_name;
		Double att_weight;
		for (Map.Entry<String, AttributeDesc> att : attributeDescMap.entrySet()) {
			att_name = att.getKey();
			if (json.containsKey(att_name)){
				fct.setWeight(att_name, (Number) json.get(att_name));
			}
		}

		projectAccessService.getProject().save();
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

    @Operation(summary = ADD_NEURAL_AMALGAMATION_FUNCTION, operationId = ADD_NEURAL_AMALGAMATION_FUNCTION)
    @RequestMapping(method = RequestMethod.PUT, 
    	path=PATH_CONCEPT_ID + "/neuralAmalgamationFunctions/{amalgamationFunctionID}", 
    	produces = APPLICATION_JSON)
    @ApiResponsesDefault
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
	Concept concept = projectAccessService.getProject().getSubConcepts().get(conceptID);
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
    @Operation(summary = DELETE_AMALGAMATION_FUNCTION, operationId = DELETE_AMALGAMATION_FUNCTION)
    @RequestMapping(method = RequestMethod.DELETE, path=PATH_CONCEPT_AMAL_FUNCTION_ID, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteAmalgamationFunction(
	    @PathVariable(value=CONCEPT_ID) String conceptID,
	    @PathVariable(value=AMAL_FUNCTION) String amalgamationFunction) {
	
	Concept thisconcept = projectAccessService.getProject().getAllSubConcepts().get(conceptID);
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
    @Operation(summary = GET_All_CONCEPTS, operationId = GET_All_CONCEPTS)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public Set<String> getConcepts() {
	Project project = projectAccessService.getProject();
	Set<String> concepts = project.getSubConcepts().keySet();        
	return concepts;
    }

    //Delete all concepts
    @Operation(summary = DELETE_ALL_CONCEPTS, operationId = DELETE_ALL_CONCEPTS)
    @RequestMapping(method = RequestMethod.DELETE, path=PATH_CONCEPTS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public boolean deleteConcepts() {
	
	return conceptService.deleteAllConcepts();
    }

    //Delete one concept
    @Operation(summary = DELETE_CONCEPT_BY_ID, operationId = DELETE_CONCEPT_BY_ID)
    @RequestMapping(method=RequestMethod.DELETE, value = PATH_CONCEPT_ID)
    @ApiResponsesDefault
    public boolean deleteConcept(
	    @PathVariable(value=CONCEPT_ID) String conceptID){
	
	Project p = projectAccessService.getProject();
	logger.info("deleting concept with id:"+conceptID);
	Concept c = p.getSubConcepts().get(conceptID);
	//TODO: this should be filtered by concept...
	for(String cb : p.getCaseBases().keySet())
	    p.deleteCaseBase(cb);
	c.getSuperConcept().removeSubConcept(conceptID);
	return true;
    }

    //add one concept
    @Operation(summary = ADD_CONCEPT_ID, operationId = ADD_CONCEPT_ID)
    @RequestMapping(method=RequestMethod.PUT, value = PATH_CONCEPT_ID)
    @ApiResponsesDefault
    public boolean addConcept(
	    @PathVariable(value=CONCEPT_ID) String conceptID){
	
	return null != conceptService.addConcept(conceptID);
    }
}
