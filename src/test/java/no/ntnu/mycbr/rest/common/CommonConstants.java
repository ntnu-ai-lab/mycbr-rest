package no.ntnu.mycbr.rest.common;

public class CommonConstants {

    // Path variable for concept
    public static final String PATH_CONCEPT = "/concepts/";

    // Path variable for casebase
    public static final String PATH_CASEBASE = "/casebases/";

    // Path variable for casebase (Method getCaseBases uses "casebase" in path, not "casebases)
    public static final String PATH_CASEBASE_SINGULAR = "/casebase";

    // Path variable for instances
    public static final String PATH_INSTANCES = "/instances/";

    // Path variable for cases
    public static final String PATH_CASES = "/cases/";

    // Path variable for delete
    public static final String PATH_DELETE = "/delete/";

    // Path variable for retrievalByIDsInIDs used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_IDS_IN_IDS = "/retrievalByIDsInIDs";

    // Path variable for retrievalByAttribute used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_ATTRIBUTE = "/retrievalByAttribute";

    // Path variable for retrievalByAttributeWithContent used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_ATTRIBUTE_WITH_CONTENT = "/retrievalWithContent";

    // Path variable for retrievalByID used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_ID = "/retrievalByID";

    // Path variable for retrievalByIDs used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_IDs = "/retrievalByIDs";

    // Path variable for retrievalByIDWithContent used in Retrieval tests
    public static final String PATH_RETRIEVAL_BY_ID_WITH_CONTENT = "/retrievalByIDWithContent";

    // Name for case base testing
    public static final String CASE_BASE_NAME = "testCaseBase";

    // Name for second case base
    public static final String CASE_BASE_NAME_SECOND = "secondTestCaseBase";

    // Name for concept testing
    public static final String CONCEPT_NAME = "testConcept";

    // Name for concept in Retrieval test: "car"
    public static final String CAR_CONCEPT_NAME = "car";

    // Name for case base used in Retrieval test: "CaseBase0"
    public static final String CAR_CASE_BASE = "CaseBase0";

    // Root and two steps into the JSON file.
    public static final String JSON_PATH = "$..";

}
