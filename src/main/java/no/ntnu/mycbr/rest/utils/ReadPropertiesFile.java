package no.ntnu.mycbr.rest.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import static no.ntnu.mycbr.rest.utils.Constants.*;

public class ReadPropertiesFile {

    private static final Logger LOG = Logger.getLogger(ReadPropertiesFile.class.getName());

    private static Properties properties = new Properties();

    private static String properitiesFile = PROPERITIES_FILE;

    static {
        properties = loadProperties(DATA_PATH + properitiesFile);
    }

    public static Properties getProperties() {
        return properties;
    }

    private static Properties loadProperties(String fileName) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(fileName);

            // load a properties file
            prop.load(input);

            // get the property value and print it out
//			System.out.println(PROJECT_NAME);
//			System.out.println(CONCEPT_NAME);
//			System.out.println(CASEBASE_NAME);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }
}
