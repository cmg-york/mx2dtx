package ca.yorku.cmg.istardt.reader.inputs.drawio;

import java.util.*;

public class InitializationParser {
    private ArrayList<String> propositions = new ArrayList<>();
    private HashMap<String, String> assignments = new HashMap<>();
    
    public void parse(String input) {
            for (String part : input.split("\\s*,\\s*")) {
            if (part.contains(":=")) {
                String[] keyValue = part.split("\\s*:=\\s*", 2);
                if (keyValue.length == 2) {
                    assignments.put(keyValue[0], keyValue[1]);
                }
            } else {
                propositions.add(part.trim());
            }
        }
    }
    
    
    public ArrayList<String> getPropositions(){
    	return propositions;
    }
    
    public HashMap<String,String> getVariables(){  
    	return assignments;
    }
    
    public void debugPrint() {
    	System.out.println("Propositions: " + propositions);
    	System.out.println("Assignments: " + assignments);
    }
}

