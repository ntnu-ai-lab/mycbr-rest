package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
<<<<<<< HEAD
import no.ntnu.mycbr.CBREngine;
=======
import no.ntnu.mycbr.rest.cbr.CBREngine;

import java.io.File;

>>>>>>> master
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableSwagger2
public class App {

    private static final Log logger = LogFactory.getLog(App.class);

    public static Project getProject() {
<<<<<<< HEAD
        if (project == null) {
            startCBR();
        }
        return project;
=======
	if (project == null) {
	    startCBR();
	}
	return project;
>>>>>>> master
    }

    private static CBREngine engine;
    private static Project project;

    public static void main(String[] args) {
<<<<<<< HEAD
        System.out.println("\n Starting the myCBR");
        startCBR();
        SpringApplication.run(App.class, args);
    }

    public static void startCBR() {
        engine = new CBREngine();
        String userDefinedProjectFile = System.getProperty("MYCBR.PROJECT.FILE");

        logger.info("The myCBR project file : " + userDefinedProjectFile);

        if (userDefinedProjectFile != null && userDefinedProjectFile.length() > 0) {
            logger.info("\n ******************* Loading the myCBR project from : " + userDefinedProjectFile
                    + " ********************\n");
            File projectFile = new File(userDefinedProjectFile);

            if (projectFile.exists())
                project = engine.createProjectFromPRJ(userDefinedProjectFile);
            else
                logger.error("\n The myCBR project file : " + userDefinedProjectFile
                        + " Does not exist ???? \n");
        } else {
            project = engine.createemptyCBRProject();
        }
=======
	System.out.println("\n Starting the myCBR");
	startCBR();
	SpringApplication.run(App.class, args);
    }

    public static void startCBR() {
	engine = new CBREngine();
	String userDefinedProjectFile = System.getProperty("MYCBR.PROJECT.FILE");

	logger.info("The myCBR project file : " + userDefinedProjectFile);

	if (userDefinedProjectFile != null && userDefinedProjectFile.length() > 0) {
>>>>>>> master

	    logger.info("\n ******************* Loading the myCBR project from : " + userDefinedProjectFile
		    + " ********************\n");
	    File projectFile = new File(userDefinedProjectFile);
	    
	    if (projectFile.exists())
		project = engine.createProjectFromPRJ(userDefinedProjectFile);
	    else
		logger.error("\n The myCBR project file : " + userDefinedProjectFile
			+ " Does not exist ???? \n");
	    
	} else {
	    logger.warn("\n ******************* Creating an empty project ??? ********************\n");
	    project = engine.createEmptyCBRProject();
	}

    }

}
