package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.HashMap;

import cmg.gReason.outputs.common.ErrorReporter;

public class IdentifierRegistry {

	private GoalModel model;
	private ErrorReporter err;
	
	// Elements found in condition boxes and anywhere else except effect elements
	private HashMap<String, String> identifiers = new HashMap<String, String>();

	// Elements found in effect elements
	private HashMap<String, Integer> effectPredicates = new HashMap<String, Integer>();
	private HashMap<String, Integer> effectVariables = new HashMap<String, Integer>();

	private ConditionExpressionParser parser;

	public IdentifierRegistry(GoalModel m, ErrorReporter e) {
		setModel(m);
		err = e;
	}
	
	public void validate() {
		
		System.out.println("" + effectPredicates);
		
		for (HashMap.Entry<String, Integer> entry : effectPredicates.entrySet()) {
		    if (!identifiers.containsKey(entry.getKey())){
		    	err.addInfo("Effect Predicate '" + entry.getKey() + "' does not appear elsewhere in the model.", "IdentifierRegistry::validate()");
		    	//Add it to the registry
		    	identifiers.put(entry.getKey(), "predicateID");
		    }
		}
		
		for (HashMap.Entry<String, Integer> entry : effectVariables.entrySet()) {
		    if (!identifiers.containsKey(entry.getKey())){
		    	err.addInfo("Variable '" + entry.getKey() + "' does not appear elsewhere in the model.", "IdentifierRegistry::validate()");
		    	identifiers.put(entry.getKey(), "variableID");
		    }
		}

		for (HashMap.Entry<String, String> entry : identifiers.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
			if ((entry.getValue().equals("predicateID")) && (!effectPredicates.containsKey(entry.getKey()))) {
		    	err.addError("Predicate '" + entry.getKey() + "' does not appear in any effect.", "IdentifierRegistry::validate()");
		    }
			
			if ((entry.getValue().equals("variableID")) && (!effectPredicates.containsKey(entry.getKey()))) {
		    	err.addError("Variable '" + entry.getKey() + "' does not appear in any effect.", "IdentifierRegistry::validate()");
		    }

		}
			
	}

	public void addEffectPredicates(ArrayList<String> addThese) {
		for (String item : addThese) {
			effectPredicates.put(item, effectPredicates.getOrDefault(item, 0) + 1);
		}
	}

	public void addEffectVariables(HashMap<String, String> addThese) {
		HashMap<String, Integer> target = new HashMap<String,Integer>();
		for (HashMap.Entry<String, String> entry : addThese.entrySet()) {
			String key = entry.getKey();
			target.put(key, target.getOrDefault(key, 0) + 1);
		}
	}


	public void setModel(GoalModel g) {
		this.model = g;
	}

	public void setParser(ConditionExpressionParser p) {
		this.parser = p;
	}

	public ConditionExpressionParser getParser() {
		return(this.parser);
	}

	public void addIdentifier(String name,String type) {
		identifiers.put(name, type);
	}

	public String getIdentifierType(String name) {
		return identifiers.get(name);
	}

	public String getIdentifierTypeBreak(String name) {
		String result = null;
		result = identifiers.get(name);
		if (result == null) {
			err.addError("Unregistered identifier '" + name + "'", "IdentifierRegistry::getIdentifierTypeBreak(String)");	
		}
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

	
	public boolean contains(String pred) {
		return(identifiers.containsKey(pred));
	}
	
	
	/**
	 * Builds a registry of identifiers and their types. It first goes over all condition boxes, which it parses.
	 * All ground elements of condition boxes are either predicates or variables. Once found, they are registered 
	 * as such. Then the remaining items of the goal model are processed and if what was known from the previous step 
	 * to be a predicate or variable is now becoming a goal, task, etc.      
	 */
	public void build() {

		// Collect items from condition boxes first
		//for (GMNode n: this.model.getGoalModel()) {
		//	if (n instanceof GMPrecondition) {
		//		System.out.println("build to parse: " + n.getLabel());
		//		parser.parse(n.getLabel());
		//	}
		//}		

		//Everything that exists in conditions is now a predicate or a variable in the registry
		//The parse routine does it..

		// Now collect all other items and add them or replace the existing ones with new type information
		for (GMNode n:this.model.getGoalModel()) {
			if (n instanceof GMTask) {
				addIdentifier(n.getCamelLabel(),"taskID");
			} else if (n instanceof GMGoal) {
				addIdentifier(n.getCamelLabel(),"goalID");
			} else if (n instanceof GMQuality) {
				addIdentifier(n.getCamelLabel(),"qualID");
			} else if (n instanceof GMEffect) {

			}
		}

	}

}
