package no.ntnu.mycbr.rest.common;

public final class Constants {


    public static final String CONCEPT_STR = "concept";
    public static final String CONCEPT_NAME_STR = "conceptName";
    
    public static final String CASEBASE_STR = "casebase";
    public static final String CASEBASE_NAME_STR = "casebaseName";
    
    public static final String CASE_NAME_STR = "caseName";
    public static final String CASE_NAMES_STR = "caseNames";
    
    public static final String INSTANCE_NAME_STR = "instanceName";
    
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
    public static final String PATH_CONCEPT_NAME = PATH_CONCEPTS + "/{conceptName}";
    
    public static final String PATH_CASEBASES = PATH_CONCEPT_NAME + "/casebases";
    public static final String PATH_CASEBASE_NAME = PATH_CASEBASES + "/{casebaseName}";
    public static final String PATH_DEFAULT_CONCEPT_CASEBASE = PATH_CASEBASE_NAME + "/";
    
    public static final String PATH_INSTANCES = PATH_CASEBASE_NAME + "/instances";
    public static final String PATH_INSTANCE_NAME = PATH_INSTANCES + "/{instanceName}";
    
    public static final String PATH_AMALGAMATION_FUNCTIONS = PATH_CONCEPT_NAME + "/amalgamationFunctions";
    public static final String PATH_AMALGAMATION_FUNCTION_NAME = PATH_AMALGAMATION_FUNCTIONS + "/{amalgamationFunctionName}";
    
    public static final String PATH_NEURAL_AMALGAMATION_FUNCTIONS = PATH_CONCEPT_NAME + "/neuralAmalgamationFunctions";
    public static final String PATH_NEURAL_AMALGAMATION_FUNCTION_NAME = PATH_AMALGAMATION_FUNCTIONS + "/{amalgamationFunctionName}";
    
    public static final String PATH_ATTRIBUTES = PATH_CONCEPT_NAME + "/attributes";
    public static final String PATH_ATTRIBUTE_NAME = PATH_ATTRIBUTES + "/{attributeName}";
    
    public static final String PATH_SIMILARITY_FUNCTIONS = PATH_ATTRIBUTE_NAME + "/similarityfunctions";
    public static final String PATH_SIMILARITY_FUNCTION_NAME = PATH_SIMILARITY_FUNCTIONS + "/{similarityFunctionName}";

    public static final String COMPUTE_SELF_SIMLARITY = "computeSelfSimlarity";
    public static final String PATH_COMPUTE_SELF_SIMLARITY = PATH_DEFAULT_CONCEPT_CASEBASE + COMPUTE_SELF_SIMLARITY;

    public static final String PATH_DEFAULT_EPHEMERAL = "/ephemeral/";
    public static final String PATH_EPHEMERAL_RETRIEVAL = PATH_DEFAULT_EPHEMERAL + "retrievalByCaseIDs";
    public static final String PATH_EPHEMERAL_COMPUTE_SELF_SIMILARITY = PATH_DEFAULT_EPHEMERAL + COMPUTE_SELF_SIMLARITY;

}
