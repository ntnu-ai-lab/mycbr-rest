import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PracticeForInheritance {
    private static final String attributeJSON = "{\"type\": \"Double\",\"solution\": \"false\",\"min\": \"0\",\"max\": \"1\"}";

    public PracticeForInheritance() {

    }
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(attributeJSON);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String type = (String) json.get("type");
        String solution = (String) json.get("solution");
        System.out.println(type);
        System.out.println(solution);
        if (type.contains("Double")){
            System.out.println("contains double");
        }

    }


}
