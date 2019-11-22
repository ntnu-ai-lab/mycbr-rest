package no.ntnu.mycbr.rest.common;

public final class Constants {
    
    
    public static final String CONCEPT_ID_STR = "conceptID";
    public static final String CASEBASE_ID_STR = "casebaseID";
    public static final String CASE_ID_STR = "caseID";
    public static final String CASE_ID_S_STR = "caseIDs";
    public static final String INSTANCE_ID_STR = "instanceID";

    
    public static final String DEFAULT_NO_OF_CASES = "-1";
    public static final String NO_OF_RETURNED_CASES = "k";

    // Need to match below variables according to the cbr project
    public static final String DEFAULT_CASEBASE = "CaseBase0"; 
    public static final String DEFAULT_CONCEPT = "Car"; 
    public static final String DEFAULT_AMALGAMATION_FUNCTION = "CarFunc"; 

    public static final String CASEBASE_STR = "casebase";	
    public static final String CASEBASE_NAME_STR = "casebaseName";	

    public static final String CONCEPT_NAME_STR = "concept name";
    public static final String AMALGAMATION_FUNCTION_STR = "amalgamation function";

    public static final String APPLICATION_JSON = "application/json";

    public static final String DEFAULT_PATH = "/concepts/{conceptID}/casebases/{casebaseID}/";

    public static final String GET_CASE_BASE_SELF_SIMILARITY = "getCaseBAseSelfSimilarity";
    public static final String SLASH_GET_CASE_BASE_SELF_SIMILARITY = "/"+GET_CASE_BASE_SELF_SIMILARITY;

}
