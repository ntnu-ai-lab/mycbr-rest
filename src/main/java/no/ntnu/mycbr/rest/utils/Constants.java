package no.ntnu.mycbr.rest.utils;

public class Constants {
    public static final String PROPERITIES_FILE = "mycbr.properties";
    public static final String DATA_PATH = System.getProperty("user.dir") + "/src/main/resources/";

    public static final String PROJECT_NAME = ReadPropertiesFile.getProperties().getProperty("PROJECT_NAME");
    public static final String PROJECT_FILE = DATA_PATH+PROJECT_NAME;

    public static final String CONCEPT_NAME = ReadPropertiesFile.getProperties().getProperty("CONCEPT_NAME");
    public static final String CASEBASE_NAME = ReadPropertiesFile.getProperties().getProperty("CASEBASE_NAME");
    public static final String AMALGAMATION_FUNCTION_NAME = ReadPropertiesFile.getProperties().getProperty("AMALGAMATION_FUNCTION_NAME");

//	public static final String CASEBASE_NAME = ReadPropertiesFile.getProperties().getProperty("CASEBASE_NAME");

    public static final String UNDEFINED = "_undefined_";
    public static final String EMPTY = "";

    /** Controller Vars **/
    private static final String CASEBASE = "support_prim_cb_latest";
    private static final String CONCEPT = "patient";
    private static final String AMALGAMATION_FUNCTION = "global_sim_1";


    public static final String SIMILARITY = "similarity";
    public static final String CASE_ID = "caseID";
    //private static final String SIMILARITY = "similarity";

}