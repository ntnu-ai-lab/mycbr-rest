package no.ntnu.mycbr.rest.utils;

import java.util.*;

/**
 * Created by jont on 23.03.2017.
 */
public class CSVTable {
    private ArrayList<String> headers = new ArrayList<String>();
    private List<List<String>> content = new ArrayList<List<String>>();

    public CSVTable() {
    }

    public CSVTable(List<Map<String, String>> jsonArrayOfFlatObjects) {
        for(Map<String, String> object : jsonArrayOfFlatObjects) {
            List<String> row = new ArrayList<String>();
            for (int i = 0; i < headers.size(); i++) {
                row.add("");
            }

            for(Map.Entry<String, String> entry : object.entrySet()) {
                if(headers.contains(entry.getKey())) {
                    int index = headers.indexOf(entry.getKey());
                    row.set(index, entry.getValue());
                } else {
                    headers.add(entry.getKey());
                    row.add(entry.getValue());
                }
            }

            content.add(row);
        }
    }

    public void addHeader(String header) {
        headers.add(header);
    }

    public void addRow(List<String> row) {
        content.add(row);
    }

    public String getTableAsString(String delimiter) {
        String table = "";

        // Headers
        for (int i = 0; i < headers.size(); i++) {
            table += "\"" + headers.get(i) + "\"";
            if(i < headers.size() - 1) { // Not last header
                table += delimiter;
            } else {    // Last row
                table += "\n";
            }
        }

        // Contents
        for(List<String> row : content) {
            for (int i = 0; i < headers.size(); i++) {
                if(i < row.size() && row.get(i) != null) {
                    table += "\"" + row.get(i) + "\"";
                } else {
                    table += "\"\"";
                }

                // Delimiter or new line
                if(i < headers.size() - 1) { // Not last attribute
                    table += delimiter;
                } else {    // Last attribute
                    table += "\n";
                }
            }
        }

        return table;
    }

    public String getTableAsString() {
        return getTableAsString(";");
    }
}
