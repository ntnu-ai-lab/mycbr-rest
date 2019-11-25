package no.ntnu.mycbr.rest.common;

public final class Constants {


    public static final String CONCEPT_STR = "concept";
    public static final String CONCEPT_ID_STR = "conceptID";
    
    public static final String CASEBASE_STR = "casebase";
    public static final String CASEBASE_ID_STR = "casebaseID";
    
    public static final String CASE_ID_STR = "caseID";
    public static final String CASE_IDS_STR = "caseIDs";
    
    public static final String INSTANCE_ID_STR = "instanceID";
    
    public static final String AMALGAMATION_FUNCTION_STR = "amalgamation function";

    public static final String DEFAULT_NO_OF_CASES = "-1";
    public static final String NO_OF_RETURNED_CASES = "k";

    // Need to match below variables according to the cbr project
    public static final String DEFAULT_CASEBASE = "CaseBase0"; 
    public static final String DEFAULT_CONCEPT = "Car"; 
    public static final String DEFAULT_AMALGAMATION_FUNCTION = "CarFunc"; 

    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT_APPLICATION_JSON = "Accept=application/json";

    
    public static final String PATH_VALUES = "/values";

    public static final String PATH_CONCEPTS = "/concepts";
    public static final String PATH_CONCEPT_ID = PATH_CONCEPTS + "/{conceptID}";
    
    public static final String PATH_CASEBASES = PATH_CONCEPT_ID + "/casebases";
    public static final String PATH_CASEBASE_ID = PATH_CASEBASES + "/{casebaseID}";
    public static final String PATH_DEFAULT_CONCEPT_CASEBASE = PATH_CASEBASE_ID + "/";
    
    public static final String PATH_INSTANCES = PATH_CASEBASE_ID + "/instances";
    public static final String PATH_INSTANCE_ID = PATH_INSTANCES + "/{instanceID}";
    
    public static final String PATH_AMALGAMATION_FUNCTIONS = PATH_CONCEPT_ID + "/amalgamationFunctions";
    public static final String PATH_AMALGAMATION_FUNCTION_ID = PATH_AMALGAMATION_FUNCTIONS + "/{amalgamationFunctionID}";
    
    public static final String PATH_NEURAL_AMALGAMATION_FUNCTIONS = PATH_CONCEPT_ID + "/neuralAmalgamationFunctions";
    public static final String PATH_NEURAL_AMALGAMATION_FUNCTION_ID = PATH_AMALGAMATION_FUNCTIONS + "/{amalgamationFunctionID}";
    
    public static final String PATH_ATTRIBUTES = PATH_CONCEPT_ID + "/attributes";
    public static final String PATH_ATTRIBUTE_ID = PATH_ATTRIBUTES + "/{attributeID}";
    
    public static final String PATH_SIMILARITY_FUNCTIONS = PATH_ATTRIBUTE_ID + "/similarityfunctions";
    public static final String PATH_SIMILARITY_FUNCTION_ID = PATH_SIMILARITY_FUNCTIONS + "/{similarityFunctionID}";

    public static final String COMPUTE_SELF_SIMLARITY = "computeSelfSimlarity";
    public static final String PATH_COMPUTE_SELF_SIMLARITY = PATH_DEFAULT_CONCEPT_CASEBASE + COMPUTE_SELF_SIMLARITY;

    public static final String PATH_DEFAULT_EPHEMERAL = PATH_CASEBASE_ID + "/ephemeral/";
    public static final String PATH_EPHEMERAL_RETRIEVAL = PATH_DEFAULT_EPHEMERAL + "retrievalByCaseIDs";
    public static final String PATH_EPHEMERAL_COMPUTE_SELF_SIMILARITY = PATH_DEFAULT_EPHEMERAL + COMPUTE_SELF_SIMLARITY;

}
