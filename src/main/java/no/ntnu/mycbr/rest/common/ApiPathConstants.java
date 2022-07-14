package no.ntnu.mycbr.rest.common;

/**
 * This interface defines constants for RequestMapping path, RequestMapping headers, PathVariable, RequestParam, and default values.
 * <br>
 * <br> <b>Abbreviations Used: </b>
 * <ul>
 * <li> ID: Identifier
 * <li> ATTR: Attribute
 * <li> SIM : Similarity
 * <li> AMAL: Amalgamation
 * </ul>
 * @author Amar Jaiswal
 * @since Dec 2, 2019
 */
public interface ApiPathConstants {

    // The media type constants
    String APPLICATION_JSON = "application/json";
    String ACCEPT_APPLICATION_JSON = "Accept=application/json";
    
    // Need to match below variables according to the cbr project
    String DEFAULT_CASEBASE = "CaseBase0"; 
    String DEFAULT_CONCEPT = "Car"; 
    String DEFAULT_AMAL_FUNCTION = "CarFunc"; 
    String DEFAULT_ATTR_ID = "Color"; 
    
    // The number of retrieved cases per query as preferred by the user.
    String DEFAULT_NO_OF_CASES = "-1";
    String NO_OF_RETURNED_CASES = "k";

    
    // myCBR-rest API: core vocabulary - single
    String CONCEPT 	= "concept";
    String CASEBASE 	= "casebase";
    String AMAL_FUNCTION= "amalgamationFunction";
    String ATTR 	= "attribute";
    String CASE 	= "case";
    String INSTANCE	= "instance";
    String VALUE 	= "value";
    String SIM_FUNCTION = "similarityFunction";

    String EPHEMERAL 	= "ephemeral";
    String ANALYTICS 	= "analytics";

    
    // myCBR-rest API: core vocabulary - multiple
    String S 			= "s";
    String CONCEPTS 		= CONCEPT 	+ S;
    String CASEBASES 		= CASEBASE 	+ S;
    String AMAL_FUNCTIONS	= AMAL_FUNCTION + S;
    String ATTRS 		= ATTR 		+ S;
    String CASES 		= CASE 		+ S;
    String INSTANCES		= INSTANCE 	+ S;
    String VALUES 		= VALUE 	+ S;
    String SIM_FUNCTIONS 	= SIM_FUNCTION	+ S;
    
    
    // myCBR-rest API: ID vocabulary
    String ID 			= "ID";
    String CONCEPT_ID 		= CONCEPT 	+ ID;
    String CASEBASE_ID 		= CASEBASE 	+ ID;
    String AMAL_FUNCTION_ID 	= AMAL_FUNCTION + ID;
    String ATTR_ID 		= ATTR 		+ ID;
    String CASE_ID 		= CASE 		+ ID;
    String INSTANCE_ID 		= INSTANCE 	+ ID;
    String SIM_FUNCTION_ID	= SIM_FUNCTION 	+ ID;
    
    
    // myCBR-rest API: type vocabulary specific names
    String TYPE 		= "Type";
    String AMAL_FUNCTION_TYPE 	= AMAL_FUNCTION + TYPE;
    String ATTR_TYPE 	       	= ATTR + TYPE;
    
    
    // Path variables
    
    // Path pattern: /___ss
    String PATH 		="/";
    String PATH_CONCEPTS 	= PATH + CONCEPTS;
    String PATH_CASEBASES 	= PATH + CASEBASES;
    String PATH_AMAL_FUNCTIONS = PATH + AMAL_FUNCTIONS;
    String PATH_CASES 		= PATH + CASES;
    String PATH_ATTRIBUTES 	= PATH + ATTRS;
    String PATH_VALUE_RANGE    = PATH + VALUE + "Range";
    String PATH_SIM_FUNCTIONS  = PATH + SIM_FUNCTIONS;
    
    String PATH_EPHEMERAL  = PATH + EPHEMERAL;
    String PATH_ANALYTICS  = PATH + ANALYTICS;
    
    
    // Path pattern: /___s/{___ID}  
    String PATH_CONCEPT_ID	  = PATH_CONCEPTS       + "/{" + CONCEPT_ID 	 + "}";
    String PATH_CASEBASE_ID 	  = PATH_CASEBASES      + "/{" + CASEBASE_ID 	 + "}";
    String PATH_AMAL_FUNCTION_ID  = PATH_AMAL_FUNCTIONS + "/{" + AMAL_FUNCTION_ID+ "}";
    String PATH_CASE_ID 	  = PATH_CASES          + "/{" + CASE_ID	 + "}"; 
    String PATH_ATTR_ID 	  = PATH_ATTRIBUTES     + "/{" + ATTR_ID         + "}";
    String PATH_SIM_FUNCTION_ID   = PATH_SIM_FUNCTIONS  + "/{" + SIM_FUNCTION_ID + "}";
    
    
    // Path pattern: /concepts/{conceptID}/___s 
    String PATH_CONCEPT_CASEBASES 	= PATH_CONCEPT_ID + PATH_CASEBASES;
    String PATH_CONCEPT_AMAL_FUNCTIONS = PATH_CONCEPT_ID + PATH_AMAL_FUNCTIONS;    
    String PATH_CONCEPT_CASES 	 	= PATH_CONCEPT_ID + PATH_CASES; 
    String PATH_CONCEPT_ATTRS 	 	= PATH_CONCEPT_ID + PATH_ATTRIBUTES;
    
    
 // Path pattern: /concepts/{conceptID}/___s/{___ID}  
    String PATH_CONCEPT_CASEBASE_ID 	 = PATH_CONCEPT_ID + PATH_CASEBASE_ID;
    String PATH_CONCEPT_AMAL_FUNCTION_ID = PATH_CONCEPT_ID + PATH_AMAL_FUNCTION_ID;
    String PATH_CONCEPT_CASE_ID     	 = PATH_CONCEPT_ID + PATH_CASE_ID;
    String PATH_CONCEPT_ATTR_ID	  	 = PATH_CONCEPT_ID + PATH_ATTR_ID;
    
    
    //Path pattern: /concepts/{conceptID}/casebases/{casebaseID}/___s
    String PATH_CONCEPT_CASEBASE_AMAL_FUNCTIONS   = PATH_CONCEPT_CASEBASE_ID + PATH_AMAL_FUNCTIONS;
    String PATH_CONCEPT_CASEBASE_CASES            = PATH_CONCEPT_CASEBASE_ID + PATH_CASES;
    
    //Path pattern: /concepts/{conceptID}/casebases/{casebaseID}/___s/{___ID}
    String PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID = PATH_CONCEPT_CASEBASE_ID + PATH_AMAL_FUNCTION_ID;
    String PATH_CONCEPT_CASEBASE_CASE_ID          = PATH_CONCEPT_CASEBASE_ID + PATH_CASE_ID;
    
    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/valueRange
    String PATH_CONCEPT_ATTR_VALUE_RANGE = PATH_CONCEPT_ATTR_ID + PATH_VALUE_RANGE;
    
    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/___s
    String PATH_CONCEPT_ATTR_SIM_FUNCTIONS = PATH_CONCEPT_ATTR_ID + PATH_SIM_FUNCTIONS;
    
    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/___s/{___ID}
    String PATH_CONCEPT_ATTR_SIM_FUNCTION_ID = PATH_CONCEPT_ATTR_ID + PATH_SIM_FUNCTION_ID;
    
    
    //Path pattern: /ephemeral/concepts/{conceptID}/amalgamationFunctions/{amalgamationFunctionID}s
    String PATH_EPHEMERAL_CONCEPT_AMAL_FUNCTION_ID = PATH_EPHEMERAL + PATH_CONCEPT_AMAL_FUNCTION_ID;
    
    //Path pattern: /analytics/concepts/{conceptID}/amalgamationFunctions/{amalgamationFunctionID}
    String PATH_ANALYTICS_CONCEPT_AMAL_FUNCTION_ID = PATH_ANALYTICS + PATH_CONCEPT_AMAL_FUNCTION_ID;
    
    String COMPUTE_SELF_SIMLARITY = "computeSelfSimilarity";
    String RETRIEVAL_BY_CASE_IDS = "retrievalByCaseIDs";
    String RETRIEVAL_BY_CASE_ID_WITH_CONTENT = "retrievalByCaseIDWithContent";
    
    // Path pattern: /___
    String PATH_SELF_SIMLARITY 		= PATH + COMPUTE_SELF_SIMLARITY;
    String PATH_RETRIEVAL 			= PATH + RETRIEVAL_BY_CASE_IDS;
    String PATH_RETRIEVAL_BY_CASE_ID_WITH_CONTENT 	= PATH + RETRIEVAL_BY_CASE_ID_WITH_CONTENT;
    
    String PATH_CONCEPT_CASEBASE_SELF_SIMLARITY = PATH_CONCEPT_CASEBASE_ID + PATH_SELF_SIMLARITY;
    
    String PATH_DEFAULT_EPHEMERAL = PATH_EPHEMERAL + PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID;
    String PATH_EPHEMERAL_RETRIEVAL = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL;
    String PATH_EPHEMERAL_RETRIEVAL_WITH_CONTENT = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL_BY_CASE_ID_WITH_CONTENT;
    String PATH_EPHEMERAL_SELF_SIMILARITY = PATH_DEFAULT_EPHEMERAL + PATH_SELF_SIMLARITY;
}
