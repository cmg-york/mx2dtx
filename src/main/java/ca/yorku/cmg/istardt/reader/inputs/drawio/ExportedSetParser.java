package ca.yorku.cmg.istardt.reader.inputs.drawio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import ca.yorku.cmg.istardt.reader.goalgraph.IdentifierRegistry;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;

public class ExportedSetParser {

	private ArrayList<String> propositions = new ArrayList<>();
	private Map<String, List<String>> relations = new HashMap<>();

	public ArrayList<String> getPropositions() {
		return propositions;
	}
	public Map<String, List<String>> getRelations() {
		return relations;
	}
	
	public void parseExportedSetLabel(String label) {

		// Tokenize the input without splitting inside parentheses
		List<String> tokens = splitTerms(label);

		for (String token : tokens) {
			token = token.trim();
			if (token.matches("^[a-zA-Z_][a-zA-Z0-9_]*\\s*\\(.*\\)$")) {
				String name = token.substring(0, token.indexOf("(")).trim();
				String argsStr = token.substring(token.indexOf("(") + 1, token.length() - 1);
				List<String> args = Arrays.asList(argsStr.split("\\s*,\\s*"));
				relations.put(name, args);
			} else {
				propositions.add(token);
			}
		}
	}

	public String constructExportedSet(Function<String , String> wrapper, String identSpace) {
		String result = "";
		for (String prop : propositions) {
			result += identSpace + "<export continuous = \"false\">" + wrapper.apply(prop) + "</export>\n";  
		}

		for (Map.Entry<String, List<String>> entry : relations.entrySet()) {
			result += identSpace + "<export continuous = \"true\" minVal = \"" + entry.getValue().get(0) +
					"\" maxVal = \"" + entry.getValue().get(1) + "\">" + 
					wrapper.apply(entry.getKey()) + "</export>\n";
		}

		return result;
	}




	// Splits top-level comma-separated terms, ignoring commas inside parentheses
	public List<String> splitTerms(String input) {
		List<String> result = new ArrayList<>();
		int depth = 0;
		StringBuilder current = new StringBuilder();

		for (char c : input.toCharArray()) {
			if (c == ',' && depth == 0) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				if (c == '(') depth++;
				if (c == ')') depth--;
				current.append(c);
			}
		}
		if (current.length() > 0) {
			result.add(current.toString());
		}
		return result;
	}

}
