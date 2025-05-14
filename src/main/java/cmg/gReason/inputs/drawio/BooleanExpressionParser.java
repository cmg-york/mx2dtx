package cmg.gReason.inputs.drawio;

import java.util.*;

public class BooleanExpressionParser {
	
	IdentifierRegistry outerOracle;

	abstract static class Node {
        abstract void printXml(StringBuilder sb, int indent);
        protected String indent(int level) {
            return "  ".repeat(level);
        }

        // Terminal tracking
        void collectTerminals(Set<String> vars, Set<String> props, boolean inComparison) {}
    }

    static class ValueNode extends Node {
        String value;
        boolean isConstant;
        boolean inComparison;

    	protected IdentifierRegistry registry;
    	
        ValueNode(String value, boolean inComparison, IdentifierRegistry identifierRegistry) {
            this.value = value;
            this.inComparison = inComparison;
            this.isConstant = value.matches("-?\\d+(\\.\\d+)?") ||
                              value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            this.registry = identifierRegistry;
        }

        void printXml(StringBuilder sb, int indent) {
            String tag = "terminal";
            String context = ( inComparison ? "In comp." : "propositional") ;
            System.out.println("Deciding now:" + value + "(" +  context + ")");
            if (value.matches("-?\\d+(\\.\\d+)?")) {
                tag = "numConst";
            } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                tag = "boolConst";
            } else {
            	if (registry.getIdentifierType(value) == null) {
            		if (inComparison) 
            			tag = "variableID";
            		else 
            			tag = "predicateID";
            	} else {
            		tag = registry.getIdentifierType(value);
            	}
            }
            System.out.println("  Verdict:" + tag);
            sb.append(indent(indent)).append("<").append(tag).append(">")
              .append(value)
              .append("</").append(tag).append(">\n");
        }

//        void collectTerminals(Set<String> vars, Set<String> props, boolean inComparison) {
//            if (!isConstant) {
//                if (inComparison) {
//                	vars.add(value);
//                	if (registry.getIdentifierType(value) == null) {
//                		registry.addIdentifier(value, "variableID");
//                	}
//                } else {
//                	props.add(value);
//                	if (registry.getIdentifierType(value) == null) {
//                		registry.addIdentifier(value, "predicateID");
//                	}
//                }
//            }
//        }
    }

    static class NotNode extends Node {
        Node child;

        NotNode(Node child) {
            this.child = child;
        }

        void printXml(StringBuilder sb, int indent) {
            sb.append(indent(indent)).append("<not>\n");
            child.printXml(sb, indent + 1);
            sb.append(indent(indent)).append("</not>\n");
        }
//
//        void collectTerminals(Set<String> vars, Set<String> props, boolean inComparison) {
//            child.collectTerminals(vars, props, false);
//        }
    }

    static class PrevNode extends Node {
        Node child;

        PrevNode(Node child) {
            this.child = child;
        }

        void printXml(StringBuilder sb, int indent) {
            sb.append(indent(indent)).append("<prev>\n");
            child.printXml(sb, indent + 1);
            sb.append(indent(indent)).append("</prev>\n");
        }

//        void collectTerminals(Set<String> vars, Set<String> props, boolean inComparison) {
//            child.collectTerminals(vars, props, false);
//        }
    }

    static class BinaryNode extends Node {
        String op;
        Node left, right;

        BinaryNode(String op, Node left, Node right) {
            this.op = op.toLowerCase();
            this.left = left;
            this.right = right;
        }

        void printXml(StringBuilder sb, int indent) {
            sb.append(indent(indent)).append("<").append(op).append(">\n");
            left.printXml(sb, indent + 1);
            right.printXml(sb, indent + 1);
            sb.append(indent(indent)).append("</").append(op).append(">\n");
        }

//        void collectTerminals(Set<String> vars, Set<String> props, boolean inComparison) {
//            left.collectTerminals(vars, props, false);
//            right.collectTerminals(vars, props, false);
//        }
    }

    static class ComparisonNode extends Node {
        String op;
        Node left, right;

        ComparisonNode(String op, Node left, Node right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        void printXml(StringBuilder sb, int indent) {
            sb.append(indent(indent)).append("<").append(op).append(">\n");
            sb.append(indent(indent + 1)).append("<left>\n");
            left.printXml(sb, indent + 2);
            sb.append(indent(indent + 1)).append("</left>\n");
            sb.append(indent(indent + 1)).append("<right>\n");
            right.printXml(sb, indent + 2);
            sb.append(indent(indent + 1)).append("</right>\n");
            sb.append(indent(indent)).append("</").append(op).append(">\n");
        }

    }

    // --- Parser ---
    static class Parser {
        private final List<String> tokens;
        private int pos = 0;
        private IdentifierRegistry oracle;
        
        Set<String> variables = new TreeSet<>();
        Set<String> propositions = new TreeSet<>();

        Parser(String input, IdentifierRegistry r) {
            this.tokens = tokenize(input);
            this.oracle = r;
        }

        public IdentifierRegistry getIdentifiers() {
        	return (oracle);
        }
        
        private List<String> tokenize(String input) {
            String spaced = input.replace("(", " ( ")
                                 .replace(")", " ) ")
                                 .replaceAll("([><=!]=?)", " $1 ")
                                 .trim();
            return Arrays.asList(spaced.split("\\s+"));
        }

        private String peek() {
            return pos < tokens.size() ? tokens.get(pos) : null;
        }

        private String next() {
            return tokens.get(pos++);
        }

        private boolean match(String... candidates) {
            String p = peek();
            if (p == null) return false;
            for (String c : candidates) {
                if (p.equalsIgnoreCase(c)) return true;
            }
            return false;
        }

        Node parse() {
            Node root = parseOr();
            //root.collectTerminals(variables, propositions, false);
            return root;
        }

        private Node parseOr() {
            Node left = parseAnd();
            while (match("OR")) {
                next();
                Node right = parseAnd();
                left = new BinaryNode("or", left, right);
            }
            return left;
        }

        private Node parseAnd() {
            Node left = parseUnary();
            while (match("AND")) {
                next();
                Node right = parseUnary();
                left = new BinaryNode("and", left, right);
            }
            return left;
        }

        private Node parseUnary() {
            if (match("NOT")) {
                next();
                return new NotNode(parseUnary());
            } else if (match("PREV")) {
                next();
                return new PrevNode(parseUnary());
            }
            return parseComparison();
        }

        private Node parseComparison() {
            Node left = parsePrimary();
            if (match("GT", "LT", "GEQ", "LEQ", "EQ", "NEQ")) {
                String op = next().toLowerCase();
                Node right = parsePrimary();
                return new ComparisonNode(op, left, right);
            }
            return left;
        }

        private Node parsePrimary() {
            if (match("(")) {
                next(); // consume "("
                Node node = parse();
                if (!match(")")) {
                    throw new RuntimeException("Expected ')'");
                }
                next(); // consume ")"
                return node;
            }
            return new ValueNode(next(), true, oracle);
        }
   
//        Set<String> getVariables() {
//            return variables;
//        }
//
//        Set<String> getPropositions() {
//            return propositions;
//        }
    }

    public IdentifierRegistry getIdentifiers() {
    	return (outerOracle);
    }
    
    public String getXML(String input, IdentifierRegistry r) {
        Parser parser = new Parser(input,r);
        Node root = parser.parse();
        outerOracle = parser.getIdentifiers();
        StringBuilder xml = new StringBuilder();
        root.printXml(xml,0);
        return xml.toString();
    }
}
