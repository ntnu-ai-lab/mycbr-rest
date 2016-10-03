package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;
import no.ntnu.mycbr.CBREngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private static LinkedList<Double> printResult(List<Pair<Instance, Similarity>> result) {
        LinkedList<Double> sims = new LinkedList<Double>();
        for (Pair<Instance, Similarity> r : result) {
            System.out.println("\nSimilarity: " + r.getSecond().getValue()
                    + " to Instance: " + r.getFirst().getName());
            sims.add(r.getSecond().getValue());
        }
        return sims;
    }


}
