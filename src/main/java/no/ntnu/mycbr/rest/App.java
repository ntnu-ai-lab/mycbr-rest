package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import no.ntnu.mycbr.CBREngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    public static Project getProject() {
        return project;
    }

    private static CBREngine engine;
    private static Project project;

    public static void main(String[] args) {

        engine = new CBREngine();
        project = engine.createProjectFromPRJ();
        SpringApplication.run(App.class, args);
    }

}
