package no.ntnu.mycbr.rest.common;

/**
 * This class defines constants for RequestMapping path, RequestMapping headers, PathVariable, RequestParam, and default values.
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
public final class ApiPathConstants {

    // The media type constants
    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT_APPLICATION_JSON = "Accept=application/json";
    
    // Need to match below variables according to the cbr project
    public static final String DEFAULT_CASEBASE = "CaseBase0"; 
    public static final String DEFAULT_CONCEPT = "Car"; 
    public static final String DEFAULT_AMAL_FUNCTION = "CarFunc"; 
    public static final String DEFAULT_ATTR_ID = "Color"; 
    
    // The number of retrieved cases per query as preferred by the user.
    public static final String DEFAULT_NO_OF_CASES = "-1";
    public static final String NO_OF_RETURNED_CASES = "k";

    
    // myCBR-rest API: core vocabulary - single
    public static final String 
    CONCEPT 		= "concept",
    CASEBASE 		= "casebase",
    AMAL_FUNCTION 	= "amalgamationFunction",
    ATTR 		= "attribute",
    CASE 		= "case",
    INSTANCE		= "instance",
    VALUE 		= "value",
    SIM_FUNCTION   	= "similarityFunction";
    
    // myCBR-rest API: core vocabulary - multiple
    public static final String 
    S 			= "s",
    CONCEPTS 		= CONCEPT 	+ S,
    CASEBASES 		= CASEBASE 	+ S,
    AMAL_FUNCTIONS	= AMAL_FUNCTION + S,
    ATTRS 		= ATTR 		+ S,
    CASES 		= CASE 		+ S,
    INSTANCES		= INSTANCE 	+ S,
    VALUES 		= VALUE 	+ S,
    SIM_FUNCTIONS 	= SIM_FUNCTION	+ S;
    
    
    // myCBR-rest API: ID vocabulary
    public static final String 
    ID 			= "ID",
    CONCEPT_ID 		= CONCEPT 	+ ID,
    CASEBASE_ID 	= CASEBASE 	+ ID,
    AMAL_FUNCTION_ID 	= AMAL_FUNCTION + ID,
    ATTR_ID 		= ATTR 		+ ID,
    CASE_ID 		= CASE 		+ ID,
    INSTANCE_ID 	= INSTANCE 	+ ID,
    SIM_FUNCTION_ID	= SIM_FUNCTION 	+ ID;
    
    
 // myCBR-rest API: IDs vocabulary
    public static final String 
    CASE_IDS 			= CONCEPT + "IDs";
    
    
    // myCBR-rest API: type vocabulary specific names
    public static final String 
    TYPE 		= "Type",
    AMAL_FUNCTION_TYPE 	= AMAL_FUNCTION + TYPE,
    ATTR_TYPE 	       	= ATTR + TYPE;

    
    // Path variables
    
    // Path pattern: /___ss
    public static final String 
    PATH 		="/",
    PATH_CONCEPTS 	= PATH + CONCEPTS,
    PATH_CASEBASES 	= PATH + CASEBASES,
    PATH_AMAL_FUNCTIONS = PATH + AMAL_FUNCTIONS,
    PATH_CASES 		= PATH + CASES,
    PATH_ATTRIBUTES 	= PATH + ATTRS,
    PATH_VALUE_RANGE    = PATH + VALUE + "RANGE",
    PATH_SIM_FUNCTIONS  = PATH + SIM_FUNCTIONS;
   
    // Path pattern: /___s/{___ID}  
    public static final String 
    PATH_CONCEPT_ID	  = PATH_CONCEPTS       + "/{" + CONCEPT_ID 	 + "}",
    PATH_CASEBASE_ID 	  = PATH_CASEBASES      + "/{" + CASEBASE_ID 	 + "}",
    PATH_AMAL_FUNCTION_ID = PATH_AMAL_FUNCTIONS + "/{" + AMAL_FUNCTION_ID+ "}",
    PATH_CASE_ID 	  = PATH_CASES          + "/{" + CASE_ID	 + "}", 
    PATH_ATTR_ID 	  = PATH_ATTRIBUTES     + "/{" + ATTR_ID         + "}",
    PATH_SIM_FUNCTION_ID  = PATH_SIM_FUNCTIONS  + "/{" + SIM_FUNCTION_ID + "}";
    
    
    // Path pattern: /concepts/{conceptID}/___s 
    public static final String 
    PATH_CONCEPT_CASEBASES 	= PATH_CONCEPT_ID + PATH_CASEBASES,
    PATH_CONCEPT_AMAL_FUNCTIONS = PATH_CONCEPT_ID + PATH_AMAL_FUNCTIONS,    
    PATH_CONCEPT_CASES 	 	= PATH_CONCEPT_ID + PATH_CASES,  
    PATH_CONCEPT_ATTRS 	 	= PATH_CONCEPT_ID + PATH_ATTRIBUTES;
    
    
 // Path pattern: /concepts/{conceptID}/___s/{___ID}  
    public static final String 
    PATH_CONCEPT_CASEBASE_ID 	  = PATH_CONCEPT_ID + PATH_CASEBASE_ID,
    PATH_CONCEPT_AMAL_FUNCTION_ID = PATH_CONCEPT_ID + PATH_AMAL_FUNCTION_ID,
    PATH_CONCEPT_CASE_ID     	  = PATH_CONCEPT_ID + PATH_CASE_ID,
    PATH_CONCEPT_ATTR_ID	  = PATH_CONCEPT_ID + PATH_ATTR_ID;
	    
    
    //Path pattern: /concepts/{conceptID}/casebases/{casebaseID}/___s
    public static final String 
    PATH_CONCEPT_CASEBASE_AMAL_FUNCTIONS   = PATH_CONCEPT_CASEBASE_ID + PATH_AMAL_FUNCTIONS,
    PATH_CONCEPT_CASEBASE_CASES            = PATH_CONCEPT_CASEBASE_ID + PATH_CASES;
    
    //Path pattern: /concepts/{conceptID}/casebases/{casebaseID}/___s/{___ID}
    public static final String 
    PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID = PATH_CONCEPT_CASEBASE_ID + PATH_AMAL_FUNCTION_ID,
    PATH_CONCEPT_CASEBASE_CASE_ID          = PATH_CONCEPT_CASEBASE_ID + PATH_CASE_ID;
    
    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/___
    public static final String PATH_CONCEPT_ATTR_VALUE_RANGE = PATH_CONCEPT_ATTR_ID + PATH_VALUE_RANGE;

    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/___s
    public static final String PATH_CONCEPT_ATTR_SIM_FUNCTIONS = PATH_CONCEPT_ATTR_ID + PATH_SIM_FUNCTIONS;
    
    //Path pattern: /concepts/{conceptID}/attributes/{attributeID}/___s/{___ID}
    public static final String PATH_CONCEPT_ATTR_SIM_FUNCTION_ID = PATH_CONCEPT_ATTR_ID + PATH_SIM_FUNCTION_ID;
    
    
    public static final String 
    COMPUTE_SELF_SIMLARITY = "computeSelfSimlarity",
    EPHEMERAL = "ephemeral",
    RETRIEVAL_BY_CASE_IDS = "retrievalByCaseIDs",
    RETRIEVAL_BY_CASE_ID_WITH_CONTENT = "retrievalByCaseIDWithContent";
    
    // Path pattern: /___
    public static final String 
    PATH_SELF_SIMLARITY 		= PATH + COMPUTE_SELF_SIMLARITY,
    PATH_EPHEMERAL 			= PATH + EPHEMERAL,
    PATH_RETRIEVAL 			= PATH + RETRIEVAL_BY_CASE_IDS,
    PATH_RETRIEVAL_BY_CASE_ID_WITH_CONTENT 	= PATH + RETRIEVAL_BY_CASE_ID_WITH_CONTENT;
    
    public static final String PATH_CONCEPT_CASEBASE_SELF_SIMLARITY = PATH_CONCEPT_CASEBASE_ID + PATH_SELF_SIMLARITY;

    public static final String PATH_DEFAULT_EPHEMERAL = PATH_EPHEMERAL + PATH_CONCEPT_CASEBASE_AMAL_FUNCTION_ID;
    public static final String PATH_EPHEMERAL_RETRIEVAL = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL;
    public static final String PATH_EPHEMERAL_RETRIEVAL_WITH_CONTENT = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL_BY_CASE_ID_WITH_CONTENT;
    public static final String PATH_EPHEMERAL_SELF_SIMILARITY = PATH_DEFAULT_EPHEMERAL + PATH_SELF_SIMLARITY;
}
