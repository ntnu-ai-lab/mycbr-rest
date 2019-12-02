package no.ntnu.mycbr.rest.common;

public final class ApiPathConstants {


    public static final String CONCEPT_STR = "concept";
    public static final String CONCEPT_ID_STR = "conceptID";
    
    public static final String CASEBASE_STR = "casebase";
    public static final String CASEBASE_ID_STR = "casebaseID";
    
    public static final String CASE_ID_STR = "caseID";
    public static final String CASE_IDS_STR = "caseIDs";
    
    public static final String INSTANCE_ID_STR = "instanceID";
    
    public static final String AMALGAMATION_FUNCTION_ID_STR = "amalgamationFunctionID";
    
    public static final String AMALGAMATION_FUNCTION_STR = "amalgamation function";

    public static final String DEFAULT_NO_OF_CASES = "-1";
    public static final String NO_OF_RETURNED_CASES = "k";

    // Need to match below variables according to the cbr project
    public static final String DEFAULT_CASEBASE = "CaseBase0"; 
    public static final String DEFAULT_CONCEPT = "Car"; 
    public static final String DEFAULT_AMALGAMATION_FUNCTION = "CarFunc"; 

    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT_APPLICATION_JSON = "Accept=application/json";

    
    // Path variables
    // Path pattern : /___s
    public static final String 
    PATH_CONCEPTS 	= "/concepts",
    PATH_CASEBASES 	= "/casebases",
    PATH_AML_FUNCTIONS 	= "/amalgamationFunctions",
    PATH_INSTANCES 	= "/instances",
    PATH_ATTRIBUTES 	= "/attributes",
    PATH_VALUES         = "/values",
    PATH_SIM_FUNCTIONS  = "/similarityfunctions";
   
    // Path pattern : /___s/{___ID}
    public static final String 
    PATH_CONCEPT_ID	 = PATH_CONCEPTS       + "/{conceptID}",
    PATH_CB_ID 	  	 = PATH_CASEBASES      + "/{casebaseID}",
    PATH_AML_FUNCTION_ID = PATH_AML_FUNCTIONS  + "/{amalgamationFunctionID}",
    PATH_INSTANCE_ID 	 = PATH_INSTANCES      + "/{instanceID}",   
    PATH_ATTRIBUTE_ID 	 = PATH_ATTRIBUTES     + "/{attributeID}", 
    PATH_SIM_FUNCTION_ID = PATH_SIM_FUNCTIONS  + "/{similarityFunctionID}";  
    
    // Path pattern : /concepts/{conceptID}/___s/{___ID}  
    public static final String 
    PATH_CONCEPT_CASEBASES 	 = PATH_CONCEPT_ID + PATH_CASEBASES,
    PATH_CONCEPT_CB_ID           = PATH_CONCEPT_ID + PATH_CB_ID,
    PATH_CONCEPT_AML_FUNCTIONS   = PATH_CONCEPT_ID + PATH_AML_FUNCTIONS,
    PATH_CONCEPT_AML_FUNCTION_ID = PATH_CONCEPT_ID + PATH_AML_FUNCTION_ID,
    PATH_CONCEPT_INSTANCES 	 = PATH_CONCEPT_ID + PATH_INSTANCES,
    PATH_CONCEPT_INSTANCE_ID     = PATH_CONCEPT_ID + PATH_INSTANCE_ID,
    PATH_CONCEPT_ATTRIBUTES 	 = PATH_CONCEPT_ID + PATH_ATTRIBUTES,
    PATH_CONCEPT_ATTRIBUTE_ID    = PATH_CONCEPT_ID + PATH_ATTRIBUTE_ID,
    PATH_CONCEPT_SIM_FUNCTIONS   = PATH_CONCEPT_ID + PATH_SIM_FUNCTIONS,
    PATH_CONCEPT_SIM_FUNCTION_ID = PATH_CONCEPT_ID + PATH_SIM_FUNCTION_ID;
    
    //Path pattern : /concepts/{conceptID}/casebases/{casebaseID}/___s/{___ID}
    public static final String 
    PATH_CONCEPT_CB_AML_FUNCTIONS   = PATH_CONCEPT_CB_ID + PATH_AML_FUNCTIONS,
    PATH_CONCEPT_CB_AML_FUNCTION_ID = PATH_CONCEPT_CB_ID + PATH_AML_FUNCTION_ID,
    PATH_CONCEPT_CB_INSTANCES       = PATH_CONCEPT_CB_ID + PATH_INSTANCES,
    PATH_CONCEPT_CB_INSTANCE_ID     = PATH_CONCEPT_CB_ID + PATH_INSTANCE_ID,
    PATH_CONCEPT_CB_ATTRIBUTES      = PATH_CONCEPT_CB_ID + PATH_ATTRIBUTES,
    PATH_CONCEPT_CB_ATTRIBUTE_ID    = PATH_CONCEPT_CB_ID + PATH_ATTRIBUTE_ID,
    PATH_CONCEPT_CB_SIM_FUNCTIONS   = PATH_CONCEPT_CB_ID + PATH_SIM_FUNCTIONS,
    PATH_CONCEPT_CB_SIM_FUNCTION_ID = PATH_CONCEPT_CB_ID + PATH_SIM_FUNCTION_ID;
    
    
    public static final String 
    COMPUTE_SELF_SIMLARITY_STR = "computeSelfSimlarity",
    EPHEMERAL_STR = "ephemeral",
    RETRIEVAL_STR = "retrievalByCaseIDs",
    RETRIEVAL_WITH_CONTENT_STR = "retrievalByIDWithContent";
    
    // Path pattern : /___
    public static final String 
    PATH_SELF_SIMLARITY 	= "/" + COMPUTE_SELF_SIMLARITY_STR,
    PATH_EPHEMERAL 		= "/" + EPHEMERAL_STR,
    PATH_RETRIEVAL 		= "/" + RETRIEVAL_STR,
    PATH_RETRIEVAL_WITH_CONTENT = "/" + RETRIEVAL_WITH_CONTENT_STR;
    
    public static final String PATH_CONCEPT_CB_SELF_SIMLARITY = PATH_CONCEPT_CB_ID + PATH_SELF_SIMLARITY;

    public static final String PATH_DEFAULT_EPHEMERAL = PATH_CONCEPT_CB_AML_FUNCTION_ID + PATH_EPHEMERAL;
    public static final String PATH_EPHEMERAL_RETRIEVAL = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL;
    public static final String PATH_EPHEMERAL_RETRIEVAL_WITH_CONTENT = PATH_DEFAULT_EPHEMERAL + PATH_RETRIEVAL_WITH_CONTENT;
    public static final String PATH_EPHEMERAL_SELF_SIMILARITY = PATH_DEFAULT_EPHEMERAL + PATH_SELF_SIMLARITY;

}
