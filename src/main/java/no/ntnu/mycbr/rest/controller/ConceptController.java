package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.model.*;
import no.ntnu.mycbr.core.similarity.*;
import no.ntnu.mycbr.core.similarity.config.AmalgamationConfig;
import no.ntnu.mycbr.rest.*;
import no.ntnu.mycbr.rest.controller.service.ConceptService;

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

    private final Log logger = LogFactory.getLog(getClass());
    private String file_path =  System.getProperty("java.io.tmpdir");
    @Autowired
    private ConceptService conceptService;

    //get all amalgamation functions
    @ApiOperation(value = GET_ALL_AMALGAMATION_FUNCTIONS, nickname = GET_ALL_AMALGAMATION_FUNCTIONS)
    //@RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPTS + "/{concept}/amalgamationFunctions", produces = APPLICATION_JSON)
    @RequestMapping(method = RequestMethod.GET, path=PATH_CONCEPT_AMAL_FUNCTIONS, produces = APPLICATION_JSON)
    @ApiResponsesDefault
    public List<String> getAmalgamationFunctions(
	    @PathVariable(value=CONCEPT_ID) String conceptID) {
	
	List<String> amalgamationFunctionIDs = new LinkedList<>();
	
	Concept concept = App.getProject().getConceptByID(conceptID);
	
	List<AmalgamationFct> amalgamationFunctions = concept.getAvailableAmalgamFcts();
        for (AmalgamationFct amalgamationFunction : amalgamationFunctions) {
            amalgamationFunctionIDs.add(amalgamationFunction.getName());
        }
        
	return amalgamationFunctionIDs;
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
    @ApiResponsesDefault
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

    @ApiOperation(value = ADD_NEURAL_AMALGAMATION_FUNCTION, nickname = ADD_NEURAL_AMALGAMATION_FUNCTION)
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
    @ApiResponsesDefault
    public Set<String> getConcepts() {
	Project project = App.getProject();
	Set<String> concepts = project.getSubConcepts().keySet();        
	return concepts;
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
    @ApiResponsesDefault
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
    @ApiResponsesDefault
    public boolean addConcept(
	    @PathVariable(value=CONCEPT_ID) String conceptID){
	
	return null != conceptService.addConcept(conceptID);
    }
}
