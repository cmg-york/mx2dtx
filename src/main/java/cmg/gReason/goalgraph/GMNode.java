package cmg.gReason.goalgraph;

import java.util.ArrayList;

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
	protected String type;
	
	// For effects only
	protected String effectStatus;
	
	// For effects and quality goals
	protected float inWeight;
	
	protected GMNode parent = null;
	protected ArrayList<GMNode> orChildren  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> andChildren  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> inContr = new ArrayList<GMNode>();
	protected ArrayList<GMNode> outContr  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> inPre  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> outPre  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> inNegPre  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> outNegPre  = new ArrayList<GMNode>();
		
	/* For tasks and temporarily effect groups */
	protected ArrayList<GMNode> effects  = new ArrayList<GMNode>();
	
	/* For tasks */
	protected GMNode outEffectLink = null;
	
	/* For effects and temporarily effect groups */
	protected GMNode inEffectLink = null;

 
	
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
     * Get the effect status of the node (for effects only).
     * 
     * @return The effect status of the node: "success" of "failure"
     */
	public String getEffectStatus() {
		return effectStatus;
	}


    /**
     * Set the effect status of the node (for effects only).
     * 
     * @param effectStatus The effect status to be set, one of  "success" of "failure".
     */
	public void setEffectStatus(String effectStatus) {
		this.effectStatus = effectStatus;
	}
	
	
    /**
     * Get the incoming weight of the node (for effects only). For effects it is the probability of the effect.
     * 
     * @return The probability of the effect.
     */
	public float getInWeight() {
		return inWeight;
	}


    /**
     * Set the incoming weight of the node (for effectsonly).
     * 
     * @param inWeight The incoming weight to be set.
     */
	public void setInWeight(float inWeight) {
		this.inWeight = inWeight;
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
		return toCamelCase(label);
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
     * Get the type of the node.
     * 
     * @return The type of the node: "goal", "task", "quality" etc.
     */
	public String getType() {
		return type;
	}

    /**
     * Set the type of the node.
     * 
     * @param type The type to be set for the node: "goal", "task", "quality" etc.
     */
	public void setType(String type) {
		this.type = type;
	}
	
    /**
     * Set the parent node of this node.
     * 
     * @param parent The parent node to be set.
     */
	public void setParent(GMNode parent) {
		this.parent = parent;
	}

    /**
     * Add an AND child to the node.
     * 
     * @param e The child node to be added.
     */
	public void addANDChild(GMNode e) {
		andChildren.add(e);
	}
	
    /**
     * Add an OR child to the node.
     * 
     * @param e The child node to be added.
     */
	public void addORChild(GMNode e) {
		orChildren.add(e);
	}
	
    /**
     * Add a node as an incoming contribution to this node.
     * 
     * @param e The contribution node to be added. A {@linkplain Contribution} object.
     */
	public void addInContribution(GMNode e) {
		inContr.add(e);
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
	
	
    /**
     * Add a node as an effect to this node (for tasks and temporarily effect groups).
     * 
     * @param e The effect node to be added.
     */
	public void addEffect(GMNode e) {
		effects.add(e);
	}
	
	 /**
     * Set the list of effect nodes for this node (for tasks and temporarily effect groups).
     * 
     * @param effs The list of effect nodes to be set.
     */
	public void setEffects(ArrayList<GMNode> effs) {
		this.effects = effs;
	}
	
	   /**
     * Set a node as the incoming effect link for this node (for effects and temporarily effect groups).
     * 
     * @param e The incoming effect link node to be set.
     */
	public void setInEffectLink(GMNode e) {
		this.inEffectLink = e;
	}
	
    /**
     * Get the incoming effect link node for this node (for effects and temporarily effect groups).
     * 
     * @return The incoming effect link node.
     */
	public GMNode getInEffectLink() {
		return(this.inEffectLink);
	}
	
    /**
     * Set a node as the outgoing effect link for this node (for tasks).
     * 
     * @param e The outgoing effect link node to be set.
     */
	public void setOutEffectLink(GMNode e) {
		this.outEffectLink = e;
	}
	
    /**
     * Get the outgoing effect link node for this node (for tasks).
     * 
     * @return The outgoing effect link node.
     */
	public GMNode getOutEffectLink() {
		return(this.outEffectLink);
	}

	
	
	
	
	/**
	 * Read Node Information
	 */
	
	public GMNode getParent() {
		return this.parent;
	};

	public ArrayList<GMNode> getORChildren(){
		return this.orChildren;
	}
	
	public ArrayList<GMNode> getANDChildren(){
		return this.andChildren;
	}
	
	public ArrayList<GMNode> getIncompingContributions(){
		return this.inContr;
	}
	
	public ArrayList<GMNode> getOutgoingContributions(){
		return this.outContr;
	}
	
	public ArrayList<GMNode> getIncompingPrecedences(){
		return this.inPre;
	}
	
	public ArrayList<GMNode> getOutgoingPrecedences(){
		return this.outPre;
	}
	
	public ArrayList<GMNode> getEffects(){
		return this.effects;
	}
	
	
	
	public String toCamelCase(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		// Keep only letters and digits, treat others as word separators
		String[] parts = input.split("[^a-zA-Z0-9]+");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.isEmpty()) continue;

			if (i == 0) {
				// First word: lowercase first letter
				sb.append(part.substring(0, 1).toLowerCase());
				sb.append(part.substring(1));
			} else {
				// Other words: capitalize first letter
				sb.append(part.substring(0, 1).toUpperCase());
				sb.append(part.substring(1));
			}
		}

		// Ensure first character is lowercase (required for Prolog atoms)
		if (sb.length() > 0) {
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}

		return sb.toString();
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
