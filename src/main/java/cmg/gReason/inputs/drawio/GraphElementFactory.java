package cmg.gReason.inputs.drawio;

import cmg.gReason.inputs.drawio.graphelementstructure.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Creates objects of the type GraphElement.
 * 
 * @author Sotirios Liaskos
  */
public class GraphElementFactory {
	/**
	 * Creates an object of type {@linkplain GraphElement} from an XML Element of type {@linkplain org.w3c.dom.Element} .
	 * 
	 * @param e The dom.Element object from where the information is extracted.
	 * @return A new GraphElement
	 * @throws Exception when the concept type is unrecognized.
	 */
	public GraphElement constructElement(Element e) throws Exception {
		
		String type = e.getAttribute("concept");
		GraphElement g;
		
		switch(type) {
		/* Entities */
		case "goal":
			//String id, String label, String actor, String notes
			g = new Goal(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("runNum")
					);
			break;
		case "task":
			g = new Task(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes")
					);
			break;
		case "quality":
			g = new Quality(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("formula"),
					e.getAttribute("dtxFormula"),
					Boolean.parseBoolean(e.getAttribute("root"))
					);
			break;
		case "precondition":
			g = new Precondition(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("formula"),
					e.getAttribute("dtxFormula")
					);
			break;
		case "initialization":
			g = new InitializationSet(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("formula"),
					e.getAttribute("dtxFormula")
					);
			break;
		case "export":
			g = new ExportedSet(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("formula"),
					e.getAttribute("dtxFormula")
					);
			break;
		case "crossrun":
			g = new CrossRunSet(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("formula"),
					e.getAttribute("dtxFormula")
					);
			break;		
		case "effectGroup":
			g = new EffectGroup(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes")
					);
			break;
		case "effect":
			g = new Effect(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					e.getAttribute("status")
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
			//TODO: Catch unconnected connectors
        	Node n = e.getElementsByTagName("mxCell").item(0);
        	String source = n.getAttributes().getNamedItem("source").getTextContent();
        	String target = n.getAttributes().getNamedItem("target").getTextContent();
        	g = new Link(
					e.getAttribute("id"),
					e.getAttribute("label"),
					e.getAttribute("actor"),
					e.getAttribute("notes"),
					type,
					source,
					target);
			break;
		default:
			throw new Exception("Concept type " + type + " unrecognized. Please check visual element with label " + e.getAttribute("label") + " and id " + e.getAttribute("id"));
		}
		return(g);
	}
}
