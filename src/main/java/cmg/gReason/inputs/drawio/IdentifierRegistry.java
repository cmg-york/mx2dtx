package cmg.gReason.inputs.drawio;

import java.util.ArrayList;
import java.util.HashMap;

public class IdentifierRegistry {
	private HashMap<String, String> identifiers = new HashMap<String, String>();
	
	public void addIdentifier(String name,String type) {
		identifiers.put(name, type);
	}

	public String getIdentifierType(String name) {
		return identifiers.get(name);
	}
	
	public String debugPrint() {
		return identifiers.toString();
	}
	
	public ArrayList<String> getPredicates() {
		ArrayList<String> preds = new ArrayList<String>();
        for (HashMap.Entry<String, String> entry : identifiers.entrySet()) {
            if (entry.getValue().equals("predicateID")) {
            	preds.add(entry.getKey());
            }
        }
        return preds;
	}

	public ArrayList<String> getVariables() {
		ArrayList<String> vars = new ArrayList<String>();
        for (HashMap.Entry<String, String> entry : identifiers.entrySet()) {
            if (entry.getValue().equals("variableID")) {
            	vars.add(entry.getKey());
            }
        }
        return vars;
	}

	
}
