package cmg.gReason.inputs.drawio;

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
		GraphElement c = null;
		
		switch(type) {
		/* Entities */
		case "goal":
		case "task":
		case "quality":
		case "precondition":
		case "initialization":
		case "export":
		case "crossrun":
		case "effectGroup":
			c = new GraphElement(e.getAttribute("id"),type,e.getAttribute("label"),"entity",e.getAttribute("actor"));
			break;
		case "effect":
			c = new GraphElement(e.getAttribute("id"),type,e.getAttribute("label"),"entity");
			c.setStatus(e.getAttribute("status"));
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
			c = new GraphElement(e.getAttribute("id"),type,e.getAttribute("label"),"relationship",source,target);
			break;
		default:
			throw new Exception("Concept type " + type + " unrecognized. Please check visual element with label " + e.getAttribute("label") + " and id " + e.getAttribute("id"));
		}
		return(c);
	}
}
