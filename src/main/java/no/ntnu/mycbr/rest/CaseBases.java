package no.ntnu.mycbr.rest;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kerstin on 05/08/16.
 */
public class CaseBases {

    private final List<String> casebaseNames = new LinkedList<>();


    public CaseBases() {

        Project project = App.getProject();

        for (Map.Entry<String, ICaseBase> cb : project.getCaseBases().entrySet()) {
            casebaseNames.add(cb.getKey());
        }
    }

    public List<String> getCaseBases() {
        return casebaseNames;
    }
}
