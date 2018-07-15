package no.ntnu.mycbr;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.SymbolAttribute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.peer.ChoicePeer;
import java.io.File;
import java.util.logging.Logger;

public class CBREngine {

	private final Log logger = LogFactory.getLog(getClass());
	/* Get actual class name to be printed on */

	// set path to myCBR projects	
	//private static String data_path = System.getProperty("user.dir") + "/src/main/resources/";

	// set path to myCBR projects
	private static String data_path = "/tmp/";
	/* project specific: NewExampleProject*/
	// name of the project file
	private static String  projectName = "anteo-fix.prj";
	// name of the central concept
	private static String conceptName = "Car";
	// name of the csv containing the instances
	private static String csv = "cars_casebase.csv";
	// set the separators that are used in the csv file
	private static String columnseparator = ";";
	private static String multiplevalueseparator = ",";
	// name of the case base that should be used; the default name in myCBR is CB_csvImport
	private static String casebase = "CaseBase0";
	// Getter for the ConceptName meta data
	public static String getCaseBase() {
		return casebase;
	}

	public static void setCasebase(String casebase) {
		CBREngine.casebase = casebase;
	}

	public static String getProjectName() {
		return projectName;
	}	

	public static void setProjectName(String projectName) {
		CBREngine.projectName = projectName;
	}

	public static String getConceptName() {
		return conceptName;
	}

	public static void setConceptName(String conceptName) {
		CBREngine.conceptName = conceptName;
	}

	public static String getCsv() {
		return csv;
	}

	public static void setCsv(String csv) {
		CBREngine.csv = csv;
	}

	/**
	 * This methods creates a myCBR project and loads the project from a .prj file
	 */	
	public Project createProjectFromPRJ(String projectFile){
		System.out.println("Trying to load prj file with : "+projectFile+" ");

		Project project = null;

		try{

			project = new Project(projectFile);

			// Sehr wichtig hier das Warten einzubauen, sonst gibts leere 
			// Retrieval Results, weil die Faelle noch nicht geladen sind wenn das 
			// Erste Retrieval laueft		
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}		
			System.out.print("\n");	//console pretty print



		//	System.out.print("cb: " project.getCaseBases().get(CBREngine.getCaseBase()).getName());	//console pretty print
		}
		catch(Exception ex){

			logger.error("Error when loading the project",ex);
		}		
		return project;		
	}	


	/**
	 * This methods creates an EMPTY myCBR project.
	 * The specification of the project's location and according file names has to be
	 * done at the beginning of this class.
	 * @return ConceptName instance containing model, sims and cases (if available)
	 */
	public Project createemptyCBRProject(){

		Project project = null;
		try {
			// load new project
			project = this.create(data_path+"tempproject.prj");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return project;
	}
	public Project create(String filename) {
		if(filename.endsWith(".zip") || filename.endsWith(".prj")) {
			filename = filename.substring(0, filename.lastIndexOf('.'));
		}
		File file = new File(filename);
		if(file.exists()) {
			if(!file.delete()) {
				logger.error("could not delete existing temporary project file, please delete it or specify other project file via -DMYCBR.PROJECT.FILE=");
				System.exit(1);
			}
		}
		try {
			Project project = new Project();
			project.setName(file.getName());
			project.setPath(file.getParent() + "/");
			project.save();
			return project;
		} catch (Exception e) {
			logger.error("error during creating of empty project",e);
			return null;
		}
	}
}