package cmg.gReason.outputs.istardtx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cmg.gReason.goalgraph.Contribution;
import cmg.gReason.goalgraph.GMCrossRunSet;
import cmg.gReason.goalgraph.GMEffect;
import cmg.gReason.goalgraph.GMExportedSet;
import cmg.gReason.goalgraph.GMGoal;
import cmg.gReason.goalgraph.GMInitializationSet;
import cmg.gReason.goalgraph.GMNode;
import cmg.gReason.goalgraph.GMPrecondition;
import cmg.gReason.goalgraph.GMQuality;
import cmg.gReason.goalgraph.GMTask;
import cmg.gReason.goalgraph.WithFormula;
import cmg.gReason.inputs.drawio.ConditionExpressionParser;
import cmg.gReason.inputs.drawio.IdentifierRegistry;
import cmg.gReason.inputs.drawio.InitializationParser;
import cmg.gReason.outputs.common.Translator;

/**
 * 
 */
public class dtxTranslator extends Translator {


    private IdentifierRegistry identifiers = new IdentifierRegistry();
    private ConditionExpressionParser parser;
   
    private String spec = "";
	
	/**
	 * 
	 *  C O N S T R U C T O R S
	 *  
	 */
	
	
	/**
	 * Create a new empty translator.
	 */
	public dtxTranslator() {

	}
	
	/**
	 * Returns the result of the translation.
	 * @return The complete iStar-DT-X specification
	 */
	public String getSpec() {
		return spec;
	}
	
	
	/*
	 * 
	 * M A I N    C A L L 
	 * 
	 */
	
	
	/**
	 * Perform the translation of the {@linkplain GoalModel} object into a iStar-DT-X specification. 
	 */
	public void produceTranslation() {
		//First build the identifier registry based on condition boxes
		buildIdentifierRegistry();
	
		//Performs the translation
		spec = createSpec();
	}
	
	
	/**
	 * Builds a registry of identifiers and their types. It first goes over all condition boxes, which it parses.
	 * All ground elements of condition boxes are either predicates or variables. Once found, they are registered 
	 * as such. Then the remaining items of the goal model are processed and if what was known from the previous step 
	 * to be a predicate or variable is now becoming a goal, task, etc.      
	 */
	private void buildIdentifierRegistry() {

		// Collect items from condition boxes first
		for (GMNode n: this.g.getGoalModel()) {
			if (n instanceof GMPrecondition) {
				parser.parse(n.getLabel());
			}
		}		

		//Everything that exists in conditions is now a predicate or a variable in the registry
		//The parse routine does it..
		
		// Now collect all other items and add them or replace the existing ones with new type information
		for (GMNode n:this.g.getGoalModel()) {
			if (n instanceof GMTask) {
				identifiers.addIdentifier(n.getCamelLabel(),"taskID");
			} else if (n instanceof GMGoal) {
				identifiers.addIdentifier(n.getCamelLabel(),"goalID");
			} else if (n instanceof GMQuality) {
				identifiers.addIdentifier(n.getCamelLabel(),"qualID");
			} else if (n instanceof GMEffect) {
				identifiers.addIdentifier(n.getCamelLabel(),"predicateID");
			}
		}

	}
	
	
	/**
	 * The main spec writing iteration.
	 */
	public String createSpec() {
		
		String model;
		String header = "\n\n\n<!--\n*** H E A D E R ***\n-->\n\n";
		String options = "\n\n\n<!--\n*** O P T I O N S ***\n-->\n";
		String actors = "\n\n\n<!--\n*** A C T O R S ***\n-->\n<actors>\n";
		String actor = "";
		String tasks = "\n\n\n<!--\n*** T A S K S ***\n-->\n<tasks>\n";
		String goals = "\n\n\n<!--\n*** G O A L S ***\n-->\n<goals>\n";
		String qualities = "\n\n\n<!--\n*** Q U A L I T I E S ***\n-->\n<qualities>\n";
		String condBoxes = "\n\n\n<!--\n*** CO N D I T I O N   B O X E S ***\n-->\n<condBoxes>\n";
		String predicates = "\n\n\n<!--\n*** P R E D I C A T E S ***\n-->\n<predicates>\n";
		String variables = "\n\n\n<!--\n*** V A R I A B L E S ***\n-->\n<variables>\n";
		String crossRuns = "\n\n\n<!--\n*** C R O S S   R U N S  ***\n-->\n<crossRuns>\n";
		String exported = "\n\n\n<!--\n*** E X P O R T S ***\n-->\n<exportedSet>\n";
		String initializations = "\n\n\n<!--\n*** I N I T I A L I Z AT I O N S ***\n-->\n<initializations>\n";
	
		int cBoxCounter = 0;
		
		
		model = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<iStarDT xmlns=\"https://example.org/istar-dt-x\"\r\n"
				+ "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "         xsi:schemaLocation=\"https://example.org/istar-dt-x ../xsd/istar-rl-schema_v4.xsd\">";
		
		//header += "<header title = \"\"\n    author = \"\"\n    source = \"\"\n    lastUpdated = \"" + java.time.LocalDateTime.now()
	    //.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +  "\">\n</header>";
		
		header += "<header title = \"\"\n    author = \"\"\n    source = \"\"\n    lastUpdated = \"Test\">\n</header>";
		
		options += "<options continuous = \"false\"\n    infeasibleActionPenalty = \"100\">\n</options>";
		
		actor = "<actor name=\"default\" description=\"\">";
		
		
		for (GMNode n:this.g.getGoalModel()) {
			
			/* * 
			 * T A S K S  
			 */
			if (n instanceof GMTask) {
				tasks += getIndent(1) + getXMLTask(n.getCamelLabel(),n.getLabel()) + "\n" ;
				String effects = getIndent(2) + "<effectGroup>\n";

				/* * 
				 * E F F E C T S  
				 */
				for (GMNode eff:((GMTask) n).getEffects()) {
					effects += getIndent(3) + getXMLEffect(eff.getCamelLabel(),eff.getLabel(),
							getEffectStatus(((GMEffect) eff).getEffectStatus()),String.valueOf(((GMEffect) eff).getInWeight())) + "\n";
					effects += getIndent(4) + getXMLTurnsTrue(eff.getCamelLabel()) + "\n";
					
					
					if (eff.getIncomingPrecedences().size()>0) { 
						effects += getIndent(3) + "<pre>" + "\n";
						effects += getIndent(4) + constructPreFormula(eff.getIncomingPrecedences())  + "\n";
						effects += getIndent(3) + "</pre>" + "\n";
					}
					
					if (eff.getIncomingNegPrecedences().size()>0) {
						effects += getIndent(3) + "<npr>" + "\n";
						effects += getIndent(4) + constructPreFormula(eff.getIncomingNegPrecedences())  + "\n";
						effects += getIndent(3) + "</npr>" + "\n";
					}
					
					effects += getIndent(3) + getXMLEffectClose() + "\n";
				}//effects
				tasks += effects + getIndent(2) + "</effectGroup>\n";
				
				if (n.getIncomingPrecedences().size()>0) { 
					tasks += getIndent(2) + "<pre>" + "\n";
					tasks += getIndent(3) + constructPreFormula(n.getIncomingPrecedences())  + "\n";
					tasks += getIndent(2) + "</pre>" + "\n";
				}
				
				if (n.getIncomingNegPrecedences().size()>0) {
					tasks += getIndent(2) + "<npr>" + "\n";
					tasks += getIndent(3) + constructPreFormula(n.getIncomingNegPrecedences())  + "\n";
					tasks += getIndent(2) + "</npr>" + "\n";
				}
				
				tasks += getIndent(1) + getXMLTaskClose() + "\n";
				
				
			/* * 
			 * G O A L S  
			 */

			} else if (n instanceof GMGoal) {
				goals += getIndent(1) + getXMLGoal(n.getCamelLabel(),n.getLabel(),
						g.getRoot().equals(n) ? "true" : "false",
						"false", ((GMGoal) n).getRuns()) + "\n" ;
				if (!((GMGoal) n).getORChildren().isEmpty()) {
					goals += getIndent(2) + "<refinement type = \"OR\">\n";
					for (GMNode ch:((GMGoal) n).getORChildren()) {
						//if (ch.getType().equals("goal")) {
						if (ch instanceof GMGoal) {
							goals += getIndent(3) + "<childGoal ref = \"" + ch.getCamelLabel() + "\"/>\n";
						} else if (ch instanceof GMTask) {
							goals += getIndent(3) + "<childTask ref = \"" + ch.getCamelLabel() + "\"/>\n";
						} else {
							System.err.println("Child of goal should be either goal or task. " + ch.getClass().getSimpleName() + "found.");
						}
					}
					goals += getIndent(2) + "</refinement>\n";
				}
				if (!((GMGoal) n).getANDChildren().isEmpty()) {
					goals += getIndent(2) + "<refinement type = \"AND\">\n";
					for (GMNode ch:((GMGoal) n).getANDChildren()) {
						goals += getIndent(3) + "<childGoal ref = \"" + ch.getCamelLabel() + "\"/>\n";
			        }
					goals += getIndent(2) + "</refinement>\n";
				}
				
				if (n.getIncomingPrecedences().size()>0) {
					goals += getIndent(2) + "<pre>" + "\n";
					goals += getIndent(3) + constructPreFormula(n.getIncomingPrecedences())  + "\n";
					goals += getIndent(2) + "</pre>" + "\n";
				}
				
				if (n.getIncomingNegPrecedences().size()>0) {
					goals += getIndent(2) + "<npr>" + "\n";
					goals += getIndent(3) + constructPreFormula(n.getIncomingNegPrecedences())   + "\n";
					goals += getIndent(2) + "</npr>" + "\n";
				}
				
				goals += getIndent(1) + getXMLGoalClose() + "\n";

			/* * 
			 * Q U A L I T I E S  
			 */
				
			} else if (n instanceof GMQuality) {
				String form = "";
				String formEnd = "";
				int indentAdd = 0;
				
				qualities += getIndent(1) + getXMLQuality(n.getCamelLabel(),
						n.getLabel(),
						n.equals(g.getQRoot()) ? "true" : "false") + "\n" ;
				
				if (((WithFormula) n).hasDtxFormula()) {
					// HAS HARDCODED DTX FORMULA - SKIP CONTRIBUTIONS
					qualities += ((WithFormula) n).getDtxFormula() + "\n";
				} else {
					// NO HARDCODED DTX FORMULA - INFER FROM CONTRIBUTIONS
					if (((GMQuality) n).getIncompingContributions().size()>1) {
						form = getIndent(2) + "<add>\n";
						formEnd = getIndent(2) + "</add>\n";
						indentAdd = 1;
					}
					
					for (GMNode inC: ((GMQuality) n).getIncompingContributions()) {
						Contribution cont = (Contribution) inC;
						String factor = "<numConst>" + cont.getContributionWeight() + "</numConst>";
						String term = cont.getContributionOrigin().getCamelLabel();
						
						if (cont.getContributionOrigin() instanceof GMEffect) {
							term = "<predicateID>" + term + "</predicateID>"; 
						} else if (cont.getContributionOrigin()  instanceof GMGoal) {
							term = "<goalID>" + term + "</goalID>";
						} else if (cont.getContributionOrigin()  instanceof GMTask) { 
							term = "<taskID>" + term + "</taskID>";
						} else if (cont.getContributionOrigin()  instanceof GMQuality) {
							term = "<qualID>" + term + "</qualID>";
						}
						form += getIndent(2+indentAdd) + "<multiply>" + factor + term + "</multiply>\n";
					}
					qualities += form + formEnd;

				}
				
				qualities += getIndent(1) + getXMLQualityClose() + "\n";
			
			/* * 
			 * C O N D I T I O N S  
			 */
			} else if (n  instanceof GMPrecondition) {
				
				if (((WithFormula) n).hasDtxFormula()) {
					//It is a named box
					condBoxes += "<condBox name = \"" + n.getLabel() + "\">" + ((WithFormula) n).getDtxFormula() + "</condBox>\n";
				} else {
					//Label has simple formula
					condBoxes += "<condBox name = \"default" + cBoxCounter++ + "\" >\n" + getIndent(1) + parser.parse(n.getLabel()) + "</condBox>\n";	
				}
				 
			
			/* * 
			 * I N I T I A L I Z A T I O N S  
			 */
			} else if (n  instanceof GMInitializationSet) {
				initializations += constructInitialization(n.getLabel());
				
			/* * 
			 * C R O S S   R U  N S 
			 */
			} else if (n instanceof GMCrossRunSet) {
				crossRuns += constructCrossRun(n.getLabel());
				
			/* * 
			 * E X P O R T S
			 */
			} else if (n  instanceof GMExportedSet) {
				exported += constructExportedSet(n.getLabel());
			} 
		}
		
		
		/* * 
		 * P R E D I C A T E S
		 */
		for (String s : identifiers.getPredicates()) {
			predicates+= getIndent(1) + "<predicate description = \"\">" + s + "</predicate>\n";
		}

		/* * 
		 * V A R I A B L E S
		 */
		for (String s : identifiers.getVariables()) {
			variables+= getIndent(1) + "<variable description = \"\">" + s + "</variable>\n";
		}
		
		tasks += "</tasks>";
		goals += "</goals>";
		qualities += "</qualities>";
		condBoxes += "</condBoxes>";
		initializations += "</initializations>";
		crossRuns += "</crossRuns>";
		exported += "</exportedSet>";
		predicates += "</predicates>";
		variables += "</variables>";
		
		
		actor += goals + tasks + qualities + condBoxes + 
				initializations + crossRuns + exported + predicates + variables + "\n\n</actor>";
		actors += actor + "\n</actors>\n";
		
		model += header + options + actors + "</iStarDT>";
		
		return (model);
				
	}

	
	
	/**
	 * H E L P E R S   ( 1 / 2 )
	 * Various parsing helpers
	 */
	
	
	
	/**
	 * Translates a condition formula to the iStarDT-X equivalent
	 * @param s The iStarDT-V condition formula
	 * @return The iStarDT-X equivalent to the iStarDT-V formula
	 */
	private String parseConditionFormula(String s) {
		String out = parser.parse(s);
		return (out);
	}
	
	
	/**
	 * Construct the iStarDT-X version of the precondition formula
	 * @param g An arraylist of origins of PRE or NPR 
	 * @return
	 */
	private String constructPreFormula(ArrayList<GMNode> g) {
		String res = "";
		int count = 0;
		for (GMNode pre:g) {
			count++;
			if (pre instanceof GMPrecondition) {
				res += parseConditionFormula(pre.getLabel());
			} else {
				res += "<" + identifiers.getIdentifierType(pre.getCamelLabel()) + ">";
				res += pre.getCamelLabel();
				res += "</" + identifiers.getIdentifierType(pre.getCamelLabel()) + ">";
			}
		}
		
		if (count > 1) {
			res = "<and>" + res + "</and>";
		}
		return res;
	}
	
	
	/*
	 * Initializations
	 */
	
	public String constructInitialization(String initLabel) {
		String result = "";
		
		InitializationParser p = new InitializationParser();
		p.parse(initLabel);
		
		for (String prop : p.getPropositions()) {
			result += getIndent(2) + "<initialization element = \"" + prop + "\">true</initialization>\n";  
		}
		
		for (Entry<String, String> entry : p.getVariables().entrySet()) {
			result += getIndent(2) + "<initialization element = \"" + entry.getKey() + "\">" + entry.getValue() + "</initialization>\n";
		}
		return result;
	}
	
	
	
	/*
	 * Cross Runs
	 */
	
	public String constructCrossRun(String label) {
		String result = "";
		
		ArrayList<String> list = new ArrayList<>(Arrays.asList(label.split("\\s*,\\s*")));
				
		for (String element : list) {
			String tag = identifiers.getIdentifierType(element);
			result += getIndent(2) + "<crossRun><" +  tag + ">" + element + "</" + tag + "></crossRun>\n";  
		}
		
		return result;
	}

	
	/*
	 * Exported + 2 helpers
	 */
	
	public String constructExportedSet(String label) {
		String result = "";
		
        ArrayList<String> propositions = new ArrayList<>();
        Map<String, List<String>> relations = new HashMap<>();

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

		for (String prop : propositions) {
			result += getIndent(2) + "<export continuous = \"false\">" + wrapWithIdentifier(prop) + "</export>\n";  
		}
		
		for (Map.Entry<String, List<String>> entry : relations.entrySet()) {
			result += getIndent(2) + "<export continuous = \"true\" minVal = \"" + entry.getValue().get(0) +
					"\" maxVal = \"" + entry.getValue().get(1) + "\">" + 
					wrapWithIdentifier(entry.getKey()) + "</export>\n";
		}
		
		return result;
	}

	
    // Splits top-level comma-separated terms, ignoring commas inside parentheses
    public static List<String> splitTerms(String input) {
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
	
    public String wrapWithIdentifier(String name) {
    	return "<" + identifiers.getIdentifierType(name) + ">" + name + "</" + identifiers.getIdentifierType(name) + ">"; 
    }
    

	
	

	
	
	
	/**
	 * H E L P E R S   ( 2 / 2 )
	 * Various text generation helpers
	 */
	
	private String getXMLTask(String name, String description) {
		return "<task name = \"" + name + "\" description = \"" + description + "\">";
	}
	private String getXMLTaskClose() {
		return "</task>";
	}

	private String getXMLGoal(String name, String description, String root, String terminal, String episodeLength) {
		return "<goal name = \"" + name + "\"" +
			      " description = \"" + description + "\"" + 
			      " root = \"" + root + "\"" +
			      " terminal = \"" + terminal + "\"" +
			      " episodeLength = \"" + episodeLength + "\">";
	}
	
	private String getXMLGoalClose() {
		return "</goal>";
	}

	private String getXMLEffect(String name, String description, String satisfying, String probability) {
		return "<effect name = \"" + name + "_Eff\"" +
				      " description = \"" + description + "\"" + 
				      " satisfying = \"" + satisfying + "\"" +
				      " probability = \"" + probability + "\">";
	}
	
	private String getXMLEffectClose() {
		return "</effect>";
	}


	private String getXMLTurnsTrue(String name) {
		return "<turnsTrue>" + name + "</turnsTrue>";
	}


	private String getXMLQuality(String name, String description, String root) {
		return "<quality name = \"" + name + "\"" +
			      " description = \"" + description + "\"" + 
			      " root = \"" + root + "\">";
	}

	
	private String getXMLQualityClose() {
		return "</quality>";
	}

	
	private String getEffectStatus(String in) {
		return in.equals("failure") ? "false" : "true"; 
	}
	
	
	private String getIndent(int level) {
		return " ".repeat(Math.max(0, level*3));
	}
	
}