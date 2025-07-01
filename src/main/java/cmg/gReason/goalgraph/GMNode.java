package cmg.gReason.goalgraph;

import java.util.ArrayList;

import cmg.gReason.outputs.common.TextCleaner;

/**
 * 
 * Representation of a goal model node. Encapsulates relationships. 
 * This class represents nodes in a goal model and provides methods to manage their relationships,
 * such as contributions, precedences, and effects.
 * 
 * @author Sotirios Liaskos
 *
 */
public class GMNode {
	
	protected String id;
	protected String label;
	protected String actor;
	protected TextCleaner cleaner = new TextCleaner();
	
	//Goals, Tasks, Effects, Preconditions
	protected ArrayList<GMNode> outContr  = new ArrayList<GMNode>();
	
	//Goals, Tasks, Effects
	protected ArrayList<GMNode> inPre  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> inNegPre  = new ArrayList<GMNode>();
	
	//Goals, Tasks, Effects, Preconditions
	protected ArrayList<GMNode> outPre  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> outNegPre  = new ArrayList<GMNode>();
	
	
    /**
     * Get the unique ID of the node.
     * 
     * @return The ID of the node.
     */
	public String getId() {
		return id;
	}
	
    /**
     * Set the unique ID of the node.
     * 
     * @param id The ID to be set for the node.
     */
	public void setId(String id) {
		this.id = id;
	}
	
    /**
     * Get the label of the node.
     * 
     * @return The label of the node.
     */
	public String getLabel() {
		return label;
	}

	
    /**
     * Gets a camel-case representation of the label of the node.
     * 
     * @return The label of the node.
     */
	public String getCamelLabel() {
		return cleaner.toCamelCase(label);
	}
	
	    /**
     * Set the label of the node.
     * 
     * @param label The label to be set for the node.
     */
	public void setLabel(String label) {
		this.label = label;
	}

    /**
     * Add a node as an outgoing contribution to this node.
     * 
     * @param e The contribution node to be added. A {@linkplain Contribution} object.
     */
	public void addOutContribution(GMNode e) {
		outContr.add(e);
	}
	
    /**
     * Add a node as an incoming precedence to this node.
     * 
     * @param e The preceding node to be added.
     */
	public void addInPrecedence(GMNode e) {
		inPre.add(e);
	}
	
    /**
     * Add a node as an outgoing precedence from this node.
     * 
     * @param e The succeeding node to be added.
     */
	public void addOutPrecedence(GMNode e) {
		outPre.add(e);
	}
	
    /**
     * Add a node as an incoming negative precedence to this node.
     * 
     * @param e The preceding node to be added.
     */
	public void addInNegPrecedence(GMNode e) {
		inNegPre.add(e);
	}


    /**
     * Add a node as an outgoing negative precedence from this node.
     * 
     * @param e The succeeding node to be added.
     */
	public void addOutNegPrecedence(GMNode e) {
		outNegPre.add(e);
	}	

    /**
     * Get the list of incoming negative precedences to this node.
     * 
     * @return The list of incoming negative precedences.
     */
	public ArrayList<GMNode> getIncomingNegPrecedences() {
		return inNegPre;
	}
	
	
	public ArrayList<GMNode> getOutgoingContributions(){
		return this.outContr;
	}
	
	public ArrayList<GMNode> getIncomingPrecedences(){
		return this.inPre;
	}
	
	public ArrayList<GMNode> getOutgoingPrecedences(){
		return this.outPre;
	}
	
	
	
	/*
	 * 
	 * D E B U G
	 * 
	 */

	
    /**
     * Utility method to print the contents of an ArrayList of GMNode objects.
     * 
     * @param l   The GMNode ArrayList to be printed.
     * @param sep The separator to be used between items in the ArrayList.
     * @return A formatted string representing the contents of the ArrayList.
     */
	public static String debugPrintList(ArrayList<GMNode> l, String sep) {
		String result = "{";
		
		if (l==null ||l.isEmpty()) {
			return ("null");
		}
		
		for (GMNode n:l) {
			result = result + "("+ n.getLabel() +")" + sep;
		}
		return (result.substring(0, result.length() - 1) + "}");
	}
}


