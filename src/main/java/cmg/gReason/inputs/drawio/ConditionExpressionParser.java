package cmg.gReason.inputs.drawio;

import java.util.*;

import cmg.gReason.goalgraph.IdentifierRegistry;

public class ConditionExpressionParser {

    private final IdentifierRegistry identifierRegistry;
    private List<String> tokens;
    private int position;
    private boolean inComparisonContext = false;

    public ConditionExpressionParser(IdentifierRegistry registry) {
        this.identifierRegistry = registry;
    }

    public IdentifierRegistry getIdentifierRegistry() {
    	return(identifierRegistry);
    }
    
    
    /**
     * The main parser of Condition/Precondition objects. Initializes an {@linkplain IdentifierRegistry} with all
     * the individuals in the formula as predicates or variables. The registry is updated as more information about 
     * the predicates becomes known.  
     * @param input A string with the formula
     * @return An iStarDT-X representation of the formula.
     */
    public String parse(String input) {
        this.tokens = tokenize(input);
        this.position = 0;
        return parseExpression(0);
    }

    
    
    private List<String> tokenize(String input) {
        return Arrays.asList(input.replace("(", " ( ")
                                  .replace(")", " ) ")
                                  .trim()
                                  .split("\\s+"));
    }

    private static final Map<String, Integer> PRECEDENCE = Map.ofEntries(
    	    Map.entry("OR", 1),
    	    Map.entry("AND", 2),
    	    Map.entry("GT", 3),
    	    Map.entry("LT", 3),
    	    Map.entry("GE", 3),
    	    Map.entry("LE", 3),
    	    Map.entry("EQ", 3),
    	    Map.entry("NEQ", 3),
    	    Map.entry("NOT", 4),
    	    Map.entry("PREV", 4),
    	    Map.entry("PREVIOUS", 4)
    	);

    private String parseExpression(int minPrecedence) {
        String left = parsePrimary();

        while (hasNext()) {
            String op = peek();
            Integer precedence = PRECEDENCE.get(op);
            if (precedence == null || precedence < minPrecedence) break;

            next(); // consume op

            if (Set.of("GT", "LT", "GE", "LE", "EQ", "NEQ").contains(op)) {
                boolean previousContext = inComparisonContext;
                inComparisonContext = true;
                String right = parseExpression(precedence + 1);
                inComparisonContext = true;
                left = rewrapIfNeeded(left);
                inComparisonContext = previousContext;
                left = formatOperator(op, left, right);
            } else {
                String right = parseExpression(precedence + 1);
                left = formatOperator(op, left, right);
            }
        }
        return left;
    }

    private String parsePrimary() {
        String token = next();
        if (token.equals("(")) {
            String expr = parseExpression(0);
            expect(")");
            return expr;
        } else if (token.equals("NOT")) {
            String expr = parseExpression(PRECEDENCE.get(token));
            return "<" + token.toLowerCase() + ">" + expr + "</" + token.toLowerCase() + ">";
        } else if ( token.equals("PREV") || token.equals("PREVIOUS") ) {
            String expr = parseExpression(PRECEDENCE.get(token));
            return "<previous>" + expr + "</previous>";
        } else {
            return wrapValue(token, inComparisonContext);
        }
    }

    private String formatOperator(String op, String left, String right) {
        if (Set.of("GT", "LT", "GE", "LE", "EQ", "NEQ").contains(op)) {
            return "<" + op.toLowerCase() + ">"
                 + "<left>" + left + "</left>"
                 + "<right>" + right + "</right>"
                 + "</" + op.toLowerCase() + ">";
        } else {
            return "<" + op.toLowerCase() + ">" + left + right + "</" + op.toLowerCase() + ">";
        }
    }

    private String wrapValue(String value, boolean inComparison) {
        if (value.matches("\\d+(\\.\\d+)?")) {
            return "<numConst>" + value + "</numConst>";
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return "<boolConst>" + value.toLowerCase() + "</boolConst>";
        } else if (value.startsWith("<") && value.endsWith(">")) {
            return value; // already wrapped
        } else {
        	String type = identifierRegistry.getIdentifierType(value);
       		if (type == null) {
       			type = inComparison ? "variableID" : "predicateID";
        		identifierRegistry.addIdentifier(value, type);
        	}
            return "<" + type + ">" + value + "</" + type + ">";
        }
    }

    private String rewrapIfNeeded(String expr) {
        if (expr.matches("<predicateID>(.+)</predicateID>")) {
            String id = expr.replaceAll("<.*?>", "");
            identifierRegistry.addIdentifier(id, "variableID");
            return "<variableID>" + id + "</variableID>";
        }
        return expr;
    }

    private boolean hasNext() {
        return position < tokens.size();
    }

    private String peek() {
        return hasNext() ? tokens.get(position) : null;
    }

    private String next() {
        return hasNext() ? tokens.get(position++) : null;
    }

    private void expect(String expected) {
        String actual = next();
        if (!expected.equals(actual)) {
            throw new IllegalArgumentException("Expected '" + expected + "' but got '" + actual + "'");
        }
    }
}


