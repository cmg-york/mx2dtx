package ca.yorku.cmg.istardt.reader.inputs.drawio;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure.*;
import ca.yorku.cmg.istardt.reader.outputs.common.ErrorReporter;
import ca.yorku.cmg.istardt.reader.outputs.common.TextCleaner;

/**
 * Creates objects of the type {@linkplain GraphElement}
 * 
 * @author Sotirios Liaskos
 */
public class GraphElementFactory {

	private ErrorReporter err;

	public void setErrorReporter(ErrorReporter e) {
		this.err = e;
	}

	public ErrorReporter getErrorReporter() {
		return(err);
	}

	public GraphElementFactory (ErrorReporter err) {
		this.err = err;
	}

	/**
	 * Creates an object of type {@linkplain GraphElement} from an XML Element of type {@linkplain org.w3c.dom.Element} .
	 * 
	 * @param e The dom.Element object from where the information is extracted.
	 * @return A new GraphElement
	 * @throws Exception when the concept type is unrecognized.
	 */
	public GraphElement constructElement(Element e) {
		TextCleaner cl = new TextCleaner();
		GraphElement g = null;

		List<String> mustHaveLabel = List.of("goal","task","quality","effect","precondition");


		String type = e.getAttribute("concept");		

		//Process label first
		String cleanLabel = cl.clean(e.getAttribute("label"));
		String cleanCamelLabel = cl.toCamelCase(cleanLabel);

		if (!cleanLabel.equals(e.getAttribute("label"))) {
			err.addWarning("Label " + e.getAttribute("label") + " trimmed to " + cleanLabel, "GraphElementFactory::constructElement(Element)");
		}
		
		//Handle empty elements
		if (mustHaveLabel.contains(type) && cleanLabel.equals("")) {
			err.addError("Element with ID " + e.getAttribute("id") + " of type '" + type + "' has empty label.", "GraphElementFactory::constructElement(Element)");
		}

		//Qualities, Preconditions, Init etc. elements:
		String formula = cl.clean(e.getAttribute("formula"));
		String dtxFormula = e.getAttribute("dtxFormula");
		
		
		switch(type) {
		/* Entities */
		case "goal":
			g = new Goal(
					e.getAttribute("id"),
					cleanCamelLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					e.getAttribute("runs"),
					Boolean.parseBoolean(e.getAttribute("isRoot"))
					);
			break;
			
		case "task":
			g = new Task(
					e.getAttribute("id"),
					cleanCamelLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel
					);
			break;
			
		case "quality":
			//Do some error checking for qualities
			if (!e.getAttribute("root").equalsIgnoreCase("true") && 
					!e.getAttribute("root").equalsIgnoreCase("false") &&
					!e.getAttribute("root").equals("")
					) {
				err.addError(type + " " + cleanLabel + ": unrecognizable 'root' attribute. Must be 'true' or 'false'", "GraphElementFactory::constructElement(Element)");
			}

			if (e.getAttribute("dtxFormula").equals("")) {
				if (e.getAttribute("formula").equals("")) {
					err.addInfo("Quality labeled as '" + cleanLabel + "' has no custom formula. Value will be inferred by contribution links.", "GraphElementFactory::constructElement(Element)");
				} else {
					err.addInfo("Quality labeled as '" + cleanLabel + "' has custom formula which will be adopted. Label will be treated as identifier.", "GraphElementFactory::constructElement(Element)");
				}
			} else {
				if (e.getAttribute("formula").equals("")) {
					err.addInfo("Quality labeled as '" + cleanLabel + "' has custom dtxformula which will be adopted. Label will be treated as the formula.", "GraphElementFactory::constructElement(Element)");
				} else {
					err.addWarning("Quality labeled as '" + cleanLabel + "' has both custom formula and custom dtxformula. Label will be treated as identifier and dtxformula will be adopted.", "GraphElementFactory::constructElement(Element)");
				}				
			}

			
			g = new Quality(
					e.getAttribute("id"),
					cleanCamelLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					formula,
					dtxFormula,
					Boolean.parseBoolean(cl.clean(e.getAttribute("isRoot")))
					);
			//System.out.println("Quality " + g.getLabel() + " root:" + cl.clean(e.getAttribute("isRoot")));
			//System.out.println("Quality " + g.getLabel() + " root:" + Boolean.parseBoolean(cl.clean(e.getAttribute("isRoot"))));
			//System.out.println("Quality " + g.getLabel() + " root:" + ((Quality) g).isQRoot());
			break;
			
		case "precondition":
			String preLabel = "";
			if (e.getAttribute("formula").equals("")) {
				//Formula is a label.
				preLabel = cleanLabel;
				err.addInfo("Precondition labeled as '" + cleanLabel + "' has no custom formula. Label will be treated as the formula.", "GraphElementFactory::constructElement(Element)");
			} else {
				//Precondition formula is an identifier
				err.addInfo("Precondition labeled as '" + cleanLabel + "' has a custom formula, which will be adopted. Label will be converted to identifier.", "GraphElementFactory::constructElement(Element)");
				preLabel = (cleanCamelLabel.length() > 20 ? cleanCamelLabel.substring(0, 20) : cleanCamelLabel);
			}

			g = new Precondition(
					e.getAttribute("id"),
					preLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					formula,
					dtxFormula
					);
			break;
			
		case "initialization":
			g = new InitializationSet(
					e.getAttribute("id"),
					cleanLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					formula,
					dtxFormula
					);
			break;
			
		case "export":
			g = new ExportedSet(
					e.getAttribute("id"),
					cleanLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					formula,
					dtxFormula
					);
			break;

		case "crossrun":
			g = new CrossRunSet(
					e.getAttribute("id"),
					cleanLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					formula,
					dtxFormula
					);
			break;		

		case "effectGroup":
			g = new EffectGroup(
					e.getAttribute("id"),
					cleanLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes")
					);
			break;
		
		case "effect":
			if (!e.getAttribute("status").equalsIgnoreCase("attainment") && !e.getAttribute("status").equalsIgnoreCase("failure")) {
				err.addError(type + " " + cleanLabel + ": unrecognizable 'status' value. Must be one of 'attainment' or 'failure'", "GraphElementFactory::constructElement(Element)");
			}
			
			g = new Effect(
					e.getAttribute("id"),
					cleanCamelLabel,
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					cleanLabel,
					e.getAttribute("status"),
					e.getAttribute("turnsTrue"),
					e.getAttribute("turnsFalse")
					);
			break;

			/* Relationships */
		case "andDecomp":
		case "orDecomp":
		case "precedenceLink":
		case "negPrecedenceLink":
		case "effectLink":
		case "effectGroupLink":
		case "contributionLink":
			Node n = e.getElementsByTagName("mxCell").item(0);
			if (validateLink(e,n)) {
				String source = n.getAttributes().getNamedItem("source").getTextContent();
				String target = n.getAttributes().getNamedItem("target").getTextContent();
				g = new Link(
						e.getAttribute("id"),
						cleanLabel,
						e.getAttribute("actor"),
						e.getAttribute("notes"),
						type,
						source,
						target);
			}
			break;
			
		default:
			err.addError("Concept type " + type + " unrecognized. Please check visual element with label " + e.getAttribute("label") + " and id " + e.getAttribute("id"), 
					"GraphElementFactory::constructElement(Element)");
		}
		return(g);
	}


	private Boolean validateLink(Element e, Node n) {

		if (n.getAttributes().getNamedItem("source") == null) { 
			err.addError("Link with label " + e.getAttribute("label") + " and type " + e.getAttribute("concept") + " has no origin.", "GraphElementFactory::constructElement(Element)");
			return(false);
		}

		if (n.getAttributes().getNamedItem("target") == null) {
			err.addError("Link with label " + e.getAttribute("label") + " and type " + e.getAttribute("concept") + " has no destination.", "GraphElementFactory::constructElement(Element)");
			return(false);
		}

		switch (e.getAttribute("concept")) {
		case "andDecomp":
			if (!e.getAttribute("label").equalsIgnoreCase("AND")) {
				err.addWarning("AND-decomposition has incompatible label '" + e.getAttribute("label") + "'. Treated as AND-decomposition.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "orDecomp":
			if (!e.getAttribute("label").equalsIgnoreCase("OR")) {
				err.addWarning("OR-decomposition has incompatible label '" + e.getAttribute("label") + "'. Treated as OR-decomposition.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "precedenceLink":
			if (!e.getAttribute("label").equalsIgnoreCase("pre")) {
				err.addWarning("Precedence link has incompatible label '" + e.getAttribute("label") + "'. Treated as precedence link.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "negPrecedenceLink":
			if (!e.getAttribute("label").equalsIgnoreCase("npr")) {
				err.addWarning("Negative precedence link has incompatible label '" + e.getAttribute("label") + "'. Treated as negative precedence link.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "effectLink":
			if (!e.getAttribute("label").equalsIgnoreCase("eff")) {
				err.addWarning("Effect link has incompatible label '" + e.getAttribute("label") + "'. Treated as effect link.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "effectGroupLink":
			if (!isNumeric(e.getAttribute("label"))) {
				err.addWarning("Effect group appears to have a non-numeric label '" + e.getAttribute("label") + "'. Label must be a numeric.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		case "contributionLink":
			if (!isNumeric(e.getAttribute("label"))) {
				err.addError("FATAL: Contribution link with label '" + e.getAttribute("label") + "'. Label must be numeric.", "GraphElementFactory::constructElement(Element)");	
			}
			break;
		}

		return(true);
	}


	private boolean isNumeric(String str) {
		return str != null && str.matches("-?\\d+(\\.\\d+)?");
	}
}
