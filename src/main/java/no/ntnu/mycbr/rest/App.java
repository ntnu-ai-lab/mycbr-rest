package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.rest.cbr.ProjectProvider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    public static Project getProject() {
        return ProjectProvider.getInstance().getProject();
    }

    public static void main(String[] args) {
        System.out.println("\n Starting the myCBR");
        SpringApplication.run(App.class, args);
    }

}
